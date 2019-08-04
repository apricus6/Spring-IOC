package com.lqx.service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SxtApplicationContext implements ApplicationContext {
    private String xmlPath;// xml配置路径
    private List<SxtBean> sxtBeans = new ArrayList<>();// 存放解析结果

    private Map<String,Object> beans = new HashMap<>();// 存放反射生成的bean ,以id作为key 对象作为value

    public SxtApplicationContext(String xmlPath) {
        this.xmlPath = xmlPath;
        //1. 解析xml
        parseXml();
        //2. 反射生成bean对象
        initBeans();
    }
    /**
     * 反射生成bean对象
     * 技术点: 反射 容器
     */
    private void initBeans() {
        if(sxtBeans.size()>0){
            for(SxtBean sxtBean : sxtBeans){
                try {
                    beans.put(sxtBean.getId(), Class.forName(sxtBean.getClazz()).newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 解析xml文件
     * 技术: dom4j + xpath
     */
    private void parseXml() {

        if(null==xmlPath || "".equals(xmlPath)){
            System.err.println("未指定路径");
        }else{
            SAXReader reader = new SAXReader();
            try {
                // 读取xml配置
                URL url = this.getClass().getClassLoader().getResource(xmlPath);
                Document document = reader.read(url);

                // bean节点的集合
                List<Element> list = document.selectNodes("/beans/bean");
                for(Element e : list){
                    String id = e.attributeValue("id");
                    String clazz = e.attributeValue("class");
                    //System.out.println(id +" - "+clazz);
                    SxtBean sxtBean = new SxtBean();
                    sxtBean.setId(id);
                    sxtBean.setClazz(clazz);
                    sxtBeans.add(sxtBean);// 放入集合
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public Object getBean(String id) {
        return beans.get(id);
    }
}
