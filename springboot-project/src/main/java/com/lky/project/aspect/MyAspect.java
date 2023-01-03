package com.lky.project.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author: haijiao12138
 * @ClassName: WebAspect
 * @description: TODO
 * @date: 2021/8/12 19:32
 */



@Aspect
@Configuration
public class MyAspect {

    //注入RedisTemplate
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //添加缓存
    @Around("@annotation(com.lky.project.utils.AddRedis)")//环绕通知+切注解
    public  Object addRedisCache(ProceedingJoinPoint proceedingJoinPoint){
        System.out.println("==环绕通知  加入缓存==");
        /*
         * 解决序列化乱码
         * */
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        //Key=类的全限定名，Value=（ key=方法名+实参，value=数据 ）
        //获取类的全向定名  com/baizhi/impl/feedbackServiceImpl
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        StringBuilder sb = new StringBuilder();

        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        sb.append(methodName);
        //获取参数
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg: args){
            sb.append("."+arg);
        }

        //获取小key
        String key = sb.toString();
        HashOperations hash = redisTemplate.opsForHash();
        //☆判断大key中有没有小key，有：true  无：false
        Boolean aBoolean = hash.hasKey(className, key);
        Object result=null;
        Object newResult=null;
        if (aBoolean){
            //1、有数据  取出数据，返回结果
            result = hash.get(className,key);
            try {
                newResult = proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            if (!newResult.equals(result)){
                System.out.println("更新数据");
                stringRedisTemplate.delete(className);
                hash.put(className,key,newResult);
                result=newResult;
            }

            System.out.println("有数据  取出数据，返回结果:  "+methodName+"--"+result);
        }else {
            //2、没有数据  查询数据库获取结果，加入缓存，返回结果
            //①放行方法   查询数据库取出结果
            try {
                result = proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            //②获取的返回结果加入缓存
            hash.put(className,key,result);
            System.out.println("获取的返回结果加入缓存，返回结果:  "+methodName+"--"+result);
        }
        return result;
    }

    //清除缓存
    @After("@annotation(com.lky.project.utils.DelRedis)")//后置通知+切注解
    public void delXCache(JoinPoint joinPoint){
        System.out.println("==后置通知  清除缓存==");

        //Key=类的全限定名，Value=（ key=方法名+实参，value=数据 ）

        //获取类的全限定名
        String className = joinPoint.getTarget().getClass().getName();
        Boolean bBoolean = stringRedisTemplate.delete(className);
        System.out.println("删除缓存"+bBoolean);
    }
}
