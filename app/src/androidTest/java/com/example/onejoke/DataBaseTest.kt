package com.example.onejoke

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.baseframework.db.DaoSupportFactory
import com.example.baseframework.db.IDaoSupport
import com.example.onejoke.model.Person
import org.junit.Test
import org.junit.runner.RunWith

/**
 * author: created by wentaoKing
 * date: created in 2020-04-19
 * description:
 */
@RunWith(AndroidJUnit4::class)
class DataBaseTest {

    @Test
    private fun testCreateDataBase() {

        val person = Person("jwt", 123)
        DaoSupportFactory.init()
        val daoSupport = DaoSupportFactory.getDao(Person::class.java)
        testInsertDataBase(daoSupport)
    }

    private fun testInsertDataBase(daoSupport: IDaoSupport<Person>) {
        val persons = mutableListOf<Person>()
        for (i in 0..10) {
            val person = Person("wentaoking${i+1}", i + 1)
            persons.add(person)
        }
        daoSupport.insert(persons)
    }
}