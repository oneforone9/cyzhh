package com.essence.service.impl.alg.util;

import com.sun.jna.Function;
import com.sun.jna.NativeLibrary;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class JnaUtil {
    //获取c的dll位置
    static NativeLibrary library = NativeLibrary.getInstance("E:\\model\\alg\\SPRS1D.dll");
    //调用c的函数
    public static <T> T invoke(String functionName, Class<T> clazz, Object... params) {
        Function function = library.getFunction(functionName);
        Object invoke = function.invoke(clazz, params);
        return clazz.cast(invoke);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        ConnToPihe1dService_c build = ConnToPihe1dService_c.build();
        Integer integer = build.apiSprs1DOpen("E:\\model\\alg\\model\\0508.inp");
        System.out.println();
        Integer integer1 = build.apiSprs1DTimeseriesNumber();
        for (int i = 0; i < integer1; i++)
        {
            byte[] byteByReference = new byte[50];
           build.apiSprs1DTimeseriesName(i, byteByReference);
//        print(new String(byteByReference, Charset.forName("GBK")).trim());


//            build.apiSprs1DTimeseriesName(i, new ByteByReference());
//对名称进行其他存储操作
        }
        Random random = new Random();
        int num = 5 ;
        int model = 2 ;
        int model2 = 1;
        String value = "12\0";

//        System.out.println("@@#"+build.API_SPRS_1D_TIMESERIES_ADDBYINDEX(num, model, "02/01/2023\0", "00:00\0", value));
//        System.out.println("@@#"+build.API_SPRS_1D_TIMESERIES_ADDBYINDEX(num, model2, "02/01/2023\0", "01:00\0", value));
//        System.out.println("@@#"+build.API_SPRS_1D_TIMESERIES_ADDBYINDEX(num, model2, "02/01/2023\0", "02:00\0", value));
//        System.out.println("@@#"+build.API_SPRS_1D_TIMESERIES_ADDBYINDEX(num, model2, "02/01/2023\0", "03:00\0", value));
        System.out.println(build.apiSprs1DTimeseriesAdd("北小河\0", 2, "02/01/2023\0", "0:00\0", "12\0"));
        System.out.println(build.apiSprs1DTimeseriesAdd("北小河\0", 1, "02/01/2023\0", "1:00\0", "12\0"));
        System.out.println(build.apiSprs1DTimeseriesAdd("北小河\0", 1, "02/01/2023\0", "2:00\0", "12\0"));
        System.out.println(build.apiSprs1DTimeseriesAdd("北小河\0", 1, "02/01/2023\0", "3:00\0", "12\0"));
        build.apiSprs1DClose();
//        print(integer,integer1);
    }

    public static void print(Object...objects){
        for (Object o:objects ) {
            System.out.println(o);
        }
    }
}
