package com.xinput.bootbase.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * 封装mongo基础查询
 *
 * @Author: xinput
 * @Date: 2020-06-17 15:59
 */
public abstract class BaseMongoDao<T> {

    /**
     * spring mongodb　集成操作类
     */
    protected MongoTemplate mongoTemplate;

    /**
     * 获取需要操作的实体类class
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 注入mongodbTemplate
     */
    protected abstract void setMongoTemplate(MongoTemplate mongoTemplate);

    // 增

    /**
     * 保存一个对象到mongodb
     */
    public T save(T bean) {
        this.mongoTemplate.save(bean);
        return bean;
    }

    public T insert(T bean) {
        this.mongoTemplate.insert(bean);
        return bean;
    }

    /**
     * 批量新增
     */
    public void insertAll(List<T> beans) {
        this.mongoTemplate.insertAll(beans);
    }

    /**
     * 批量新增
     */
    public void insert(List<T> beans) {
        this.mongoTemplate.insert(beans);
    }

    /**
     * 删
     *
     * @param query
     */
    public void delete(Query query) {
        this.mongoTemplate.remove(query, this.getEntityClass());
    }

    public void remove(T bean) {
        this.mongoTemplate.remove(bean);
    }

    /**
     * 根据条件查询出来后 再去删除
     */
    public T findAndRemove(Query query) {
        return this.mongoTemplate.findAndRemove(query, this.getEntityClass());
    }

    public List<T> findAllAndRemove(Query query) {
        return this.mongoTemplate.findAllAndRemove(query, this.getEntityClass());
    }

    // 改 upsert 先查询，如果没有符合条件的，会执行插入，插入的值是查询值 ＋ 更新值。

    /**
     * 按条件修改,仅修改第一条数据
     */
    public void updateFirst(Query query, Update update) {
        this.mongoTemplate.updateFirst(query, update, this.getEntityClass());
    }

    /**
     * 批量修改
     */
    public void updateMulti(Query query, Update update) {
        this.mongoTemplate.updateMulti(query, update, this.getEntityClass());
    }

    /**
     * 通过条件查询更新数据
     */
    public void upsert(Query query, Update update) {
        this.mongoTemplate.upsert(query, update, this.getEntityClass());
    }

    /**
     * 查询并且修改记录
     */
    public T findAndModify(Query query, Update update) {
        return this.mongoTemplate.findAndModify(query, update, this.getEntityClass());
    }

    // 查

    /**
     * 根据ID获取记录
     */
    public T findById(String id) {
        return this.mongoTemplate.findById(id, this.getEntityClass());
    }

    /**
     * 根据Query查询唯一实体
     */
    public T findOne(Query query) {
        return this.mongoTemplate.findOne(query, this.getEntityClass());
    }

    /**
     * 查询所有数据
     */
    public List<T> findAll() {
        return this.mongoTemplate.findAll(getEntityClass());
    }

    /**
     * 根据条件查询实体的集合
     */
    public List<T> find(Query query) {
        return this.mongoTemplate.find(query, this.getEntityClass());
    }

    /**
     * 根据query 计算总量
     */
    public long count(Query query) {
        return mongoTemplate.count(query, getEntityClass());
    }

    /**
     * 分页查询
     *
     * @param query  查询条件，相当于 mysql where条件
     * @param orders 排序 ，相当于 mysql order 条件
     * @param skip   跳过 ，相当于 mysql limit m,n 中的 m
     * @param limit  取数据， 相当于 mysql limit m,n 中的 n
     * @return
     */
    public List<T> find(Query query, List<Sort.Order> orders, Integer skip, Integer limit) {
        if (null != orders && orders.size() > 0) {
            query.with(Sort.by(orders));
        }
        if (limit != null) {
            query.limit(limit);
        }

        if (skip != null) {
            query.skip(skip);
        }

        return mongoTemplate.find(query, getEntityClass());
    }
}
