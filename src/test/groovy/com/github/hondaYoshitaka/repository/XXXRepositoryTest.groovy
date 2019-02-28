package com.github.hondaYoshitaka.repository

import com.github.hondaYoshitaka.configuration.DatabaseConfiguration
import com.github.hondaYoshitaka.model.entity.XXX
import com.yo1000.dbspock.Tables
import com.yo1000.dbspock.dbunit.DbspockExpectations
import com.yo1000.dbspock.dbunit.DbspockLoaders
import org.apache.ibatis.session.RowBounds
import org.dbunit.DataSourceDatabaseTester
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Unroll

@MybatisTest
@Import([DatabaseConfiguration])
@Unroll
class XXXRepositoryTest extends Specification {
    @Autowired
    XXXRepository xxxRepository

    @Autowired
    DataSourceDatabaseTester tester

    def "1件取得_id:#keyのレコードが存在する場合、想定のフィールド値が取得できること"() {
        given:
        prepareDataSet({
            xxx {
                id | name | category_id | price | active
                0L | 'name 0' | 0L | 0 | false
                1L | 'name 1' | 10L | 100 | true
                2L | 'name 2' | 20L | 200 | true
            }
        })
        when:
        def actual = xxxRepository.findOne(key)

        then:
        verifyAll(actual) {
            it.id == _id
            it.name == _name
            it.categoryId == _categoryId
            it.price == _price
            it.active == _active
        }

        where:
        key || _id | _name    | _categoryId | _price | _active
        0L  || 0L  | 'name 0' | 0L          | 0      | false
        1L  || 1L  | 'name 1' | 10L         | 100    | true
    }

    def "1件取得_該当するidのレコードが存在しない場合、nullが取得できること"() {
        given:
        prepareDataSet({
            xxx {
                id | name | category_id | price | active
                999L | 'name 999' | 999L | 999 | true
            }
        })
        when:
        def actual = xxxRepository.findOne(key)

        then:
        assert actual == null

        where:
        key || _
        1L  || _
    }

    def "複数件取得_カテゴリID:#categoryIdと価格:#priceAで検索した場合、ID:#expectedが取得できること"() {
        given:
        prepareDataSet({
            xxx {
                id | name | category_id | price | active
                4L | 'name 4' | 0L | 200 | false
                3L | 'name 3' | 0L | 200 | true
                2L | 'name 2' | 20L | 200 | true
                1L | 'name 1' | 10L | 100 | true
                0L | 'name 0' | 0L | 0 | false
            }
        })
        when:
        def actual = xxxRepository.findAllByCategory(categoryId, priceA, RowBounds.DEFAULT)

        then:
        assert actual.stream().map { XXX xxx -> xxx.id }.collect() == expected

        where:
        categoryId | priceA || expected
        0L         | null   || [0, 3, 4]
        10L        | null   || [1]
        100L       | null   || []
        0L         | 0      || [0]
        0L         | 200    || [3, 4]
    }

    def "複数件取得_オフセット:#offsetと上限数:#limitの場合、ID:#expectedが取得できること"() {
        given:
        prepareDataSet({
            xxx {
                id | name | category_id | price | active
                4L | 'name 4' | 0L | 1 | false
                3L | 'name 3' | 0L | 1 | true
                2L | 'name 2' | 0L | 1 | false
                1L | 'name 1' | 0L | 1 | true
                0L | 'name 0' | 0L | 1 | false
            }
        })
        when:
        def actual = xxxRepository.findAllByCategory(0L, 1, new RowBounds(offset, limit))

        then:
        assert actual.stream().map { XXX xxx -> xxx.id }.collect() == expected

        where:
        offset | limit || expected
        0      | 0     || []
        0      | 1     || [0]
        0      | 4     || [0, 1, 2, 3]
        0      | 5     || [0, 1, 2, 3, 4]
        0      | 6     || [0, 1, 2, 3, 4]
        1      | 3     || [1, 2, 3]
        2      | 3     || [2, 3, 4]
        3      | 3     || [3, 4]
        4      | 3     || [4]
        5      | 3     || []
    }

    def "1件登録_"() {
        given:
        def entity = new XXX(name: _name, categoryId: _categoryId, price: _price, active: _active)

        when:
        xxxRepository.insertOne(entity)

        then:
        // TODO: dbspockの使用を見直す
        DbspockExpectations.matches(tester.connection, {
            xxx {
                name | category_id | price | active
                'name 0' | 1 | 100 | true
            }
        } as Closure<Tables>)

        where:
        _name    | _categoryId | _price | _active || _
        'name 0' | 1L          | 100    | true    || _
    }

    /**
     * databaseレコードをセットアップします.
     * --------------------------------------------
     * - データの形式は `com.yo1000.dbspock.dbunit` に準ずる
     *
     * @param data データセット
     */
    private void prepareDataSet(final Closure data) {
        tester.dataSet = DbspockLoaders.loadDataSet(data)
        tester.onSetup()
    }
}
