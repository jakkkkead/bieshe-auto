package com.example.auto.util;

import com.example.auto.bean.CommomFormBean;

import java.text.DecimalFormat;
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
    public static Double[] getSimpleNumList(List<List<String>> list,Integer index , Integer valueType){
        //List<Double> reList = new ArrayList<>(list.size());
        //初始化数组，长度为分割线之后的列长度
        Double[] reDouble = initDouble(list.get(0).size()-index);
        //y代表每一行的index
        for(int y =0 ; y<list.size();y++){
            //i代表分割线后的index，每次循环后reDouble数据 里面的值相当于当前y行之前的总和值

            for(int i = index;i<list.get(y).size();i++){
                Double min =Double.valueOf(list.get(0).get(i));
                //计算每一行的值（分割线之后）
                switch (valueType){
                    case 0:reDouble[i-index] = Double.valueOf( list.get(y).get(i))+reDouble[i-index];break;
                    case 1:min = Double.valueOf( list.get(y).get(i))<min ? min:Double.valueOf( list.get(y).get(i));
                         reDouble[i-index] = min;
                         break;
                    case 2:reDouble[i-index] = (Double.valueOf( list.get(y).get(i))+reDouble[i-index])/2;break;
                    case 3: min = Double.valueOf( list.get(y).get(i))>min ? min:Double.valueOf( list.get(y).get(i));
                          reDouble[i-index] = min;
                          break;
                    default:reDouble[i-index] = Double.valueOf( list.get(y).get(i))+reDouble[i-index];break;
                }

            }
        }
        for(int i =0 ; i< reDouble.length; i++){
            reDouble[i] = ListUtil.handlerData(reDouble[i]);
        }
        return  reDouble;
    }

    /**
     * 处理数据的小数位数
     * @param data
     * @return
     */
    public static Double handlerData(Double data){
        DecimalFormat df = new DecimalFormat("#0.00");
        Double sfDouble = Double.valueOf(data!=null?df.format(data):"0" );
        return sfDouble;
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
            reList.add(typeList.get(i));
        }
        return reList;
    }

    /**
     * 将list进行字段分组
     * @param list
     * @param x
     * @return
     */
    public static Map<String,Double>groupList(List<List<String>> list,Integer x,Integer y , Integer valueType){
        //以map数据结构，key为值相同的列值，值为list存储值相同的行值
        Map<String,List<List<String>>> map = new HashMap<>();
        Map<String,Double> reMap = new HashMap<>();
        List<List<String>> mapList ;
        for(int i =0 ;i<list.size();i++){
         //   List list1 = list.get(i);
            String categery = list.get(i).get(x);
            if(map.containsKey(categery)){
                //存在相同的列值，取出key相同的list，add 相同列值的行
                map.get(categery).add(list.get(i));
            }else{
                //不存在相同的列值，新建key，存在行值
                mapList  = new ArrayList<>();
                mapList.add(list.get(i));
                map.put(categery,mapList);
            }
        }
        for (String key : map.keySet()){
            Double res = 0.0;
            switch (valueType){
                case 0:  res = ListUtil.handlerData(ListUtil.caculateTotal(map.get(key),y));break;
                case 1:  res =ListUtil.handlerData(ListUtil.caculateMax(map.get(key),y));break;
                case 2:  res =ListUtil.handlerData(ListUtil.caculateAvg(map.get(key),y));break;
                case 3:  res =ListUtil.handlerData(ListUtil.caculateMin(map.get(key),y));break;
                default:
                        res = ListUtil.handlerData(ListUtil.caculateTotal(map.get(key),y));break;
            }
            reMap.put(key,res);
        }
        return reMap;

    }

    /**
     * 计算列值总和
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
    public static Double caculateAvg(List<List<String>> list,Integer y){
        Double sum = ListUtil.caculateTotal(list,y);
        return sum/list.size();
    }
    public static Double caculateMin(List<List<String>> list,Integer y){
        Double min = Double.valueOf(list.get(0).get(y));
        for (int i = 0; i< list.size();i++){
            if(min >= Double.valueOf(list.get(i).get(y))){
                min = Double.valueOf(list.get(i).get(y));
            }
        }
        return min;
    }
    public static Double caculateMax(List<List<String>> list,Integer y){
        Double max = 0.0;
        for (int i = 0; i< list.size();i++){
            if(max <= Double.valueOf(list.get(i).get(y))){
                max = Double.valueOf(list.get(i).get(y));
            }
        }
        return max;
    }
}
