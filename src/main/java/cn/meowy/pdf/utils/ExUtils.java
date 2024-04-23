package cn.meowy.pdf.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.function.FailableSupplier;

import java.util.function.Supplier;

/**
 * 异常工具类
 *
 * @author: Mr.Zou
 * @date: 2024/4/18
 **/
public class ExUtils {

    /**
     * 将检查异常转换为运行时异常
     *
     * @param supplier 方法
     * @param <R>      返回值类型
     * @return 返回值
     */
    public static <R, E extends Throwable> R execute(FailableSupplier<R, E> supplier) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException("捕获到异常!", e);
        }
    }

    /**
     * 将检查异常转换为运行时异常
     *
     * @param supplier 方法
     * @param errorMsg 错误描述
     * @param <R>      返回值类型
     * @return 返回值
     */
    public static <R, E extends Throwable> R execute(FailableSupplier<R, E> supplier, String errorMsg) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(errorMsg, e);
        }
    }

    /**
     * 将检查异常转换为运行时异常
     *
     * @param supplier 方法
     * @param errorMsg 错误描述
     * @param <R>      返回值类型
     * @return 返回值
     */
    public static <R, E extends Throwable> R execute(FailableSupplier<R, E> supplier, String errorMsg, Object... params) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            throw new RuntimeException(StrUtil.format(errorMsg, params), e);
        }
    }

    /**
     * 将检查异常转换为运行时异常
     *
     * @param runnable 方法
     * @param errorMsg 错误描述
     */
    public static <E extends Throwable> void execute(FailableRunnable<E> runnable, String errorMsg, Object... params) {
        try {
            runnable.run();
        } catch (Throwable e) {
            throw new RuntimeException(StrUtil.format(errorMsg, params), e);
        }
    }


}
