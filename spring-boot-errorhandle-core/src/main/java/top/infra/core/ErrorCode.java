package top.infra.core;

/**
 * Created by zhuowan on 2016/12/29 10:22.
 * Description: Web Project APIs Response bean should implements this interface
 */
public interface ErrorCode {

    String getName();

    ErrorMessage getMessage();

    int getStatusValue();
}
