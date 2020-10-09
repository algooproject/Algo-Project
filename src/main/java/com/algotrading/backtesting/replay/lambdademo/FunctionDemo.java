package com.algotrading.backtesting.replay.lambdademo;

public class FunctionDemo {

    public void aaa(SelfFunction function, int arg)
    {
        System.out.println(function.aaa(arg));
    }

    public static void main(String[] args)
    {
        FunctionDemo demo = new FunctionDemo();

        // Method 1
        demo.aaa(new MyFunction(2), 1);

        // Method 2
        int add = 2;
        demo.aaa(new SelfFunction() {
            @Override
            public String aaa(int arg) {
                return String.valueOf(arg + add);
            }
        }, 1);

        // Method 3
        demo.aaa(arg -> String.valueOf(arg + add), 1);
        demo.aaa(arg -> {return String.valueOf(arg + add);}, 1);
        // similiar to demo.aaa( [&](arg){return String.valueOf(arg);}, 1);
    }

}
class MyFunction implements SelfFunction
{
    private final int add;

    public MyFunction(int add)
    {
        this.add = add;
    }

    @Override
    public String aaa(int arg) {
        return String.valueOf(arg + add);
    }
}
