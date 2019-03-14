package com.example.auto.util;

import com.example.auto.bean.CommomFormBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListUtil {
    public static CommomFormBean getCommForm(int size){
        CommomFormBean com = new CommomFormBean();
        com.setObjects(new Object[size]);
        return com;
    }
    /**
     * 计算分割线后的值
     * @param list
     * @param index
     * @return
     */
    public static Double[] getSimpleNumList(List<List<String>> list,Integer index){
        //List<Double> reList = new ArrayList<>(list.size());
        Double[] reDouble = initDouble(list.get(0).size()-index);

        for(int y =0 ; y<list.size();y++){
            for(int i = index;i<list.get(y).size();i++){
                reDouble[i-index] = Double.valueOf( list.get(y).get(i))+reDouble[i-index];
            }
        }
        return  reDouble;
    }
    public static Double [] initDouble(int size){
        Double[] arr = new Double[size];
        for(int i=0 ; i< size ; i++){
            arr[i] = Double.valueOf(0);
        }
        return arr;
    }

    /**
     * 计算返回的类型值
     * @param typeList
     * @param index
     * @return
     */
    public static List<String> getCategery(List<String>typeList , Integer index){
        List<String> reList = new ArrayList<>();
        for(int i =index;i<typeList.size();i++){
            reList.add(typeList.get(i).toString());
        }
        return reList;
    }

    /**
     * 将list进行字段分组
     * @param list
     * @param x
     * @return
     */
    public static Map<String,Double>groupList(List<List<String>> list,Integer x,Integer y){
        Map<String,List<List<String>>> map = new HashMap<>();
        Map<String,Double> reMap = new HashMap<>();
        List<List<String>> mapList ;
        for(int i =0 ;i<list.size();i++){
            List list1 = list.get(i);
            String categery = list.get(i).get(x);
            if(map.containsKey(categery)){
                map.get(categery).add(list.get(i));
            }else{
                mapList  = new ArrayList<>();
                mapList.add(list.get(i));
                map.put(categery,mapList);
            }
        }
        for (String key : map.keySet()){
            Double res = ListUtil.caculateTotal(map.get(key),y);
            reMap.put(key,res);
        }
        return reMap;

    }

    /**
     * 计算列值
     * @param list
     * @param y
     * @return
     */
    public static Double caculateTotal(List<List<String>> list,Integer y){
        Double sum = Double.valueOf(0);
        for(int i=0;i<list.size(); i++){
            sum = sum+Double.valueOf(list.get(i).get(y));
        }
        return  sum;
    }
}
