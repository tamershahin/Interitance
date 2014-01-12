package com.grailsurfing.step1



import grails.test.mixin.*
import spock.lang.*

@TestFor(ConcordeController)
@Mock(Concorde)
class ConcordeControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.concordeInstanceList
            model.concordeInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.concordeInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            def concorde = new Concorde()
            concorde.validate()
            controller.save(concorde)

        then:"The create view is rendered again with the correct model"
            model.concordeInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            concorde = new Concorde(params)

            controller.save(concorde)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/concorde/show/1'
            controller.flash.message != null
            Concorde.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def concorde = new Concorde(params)
            controller.show(concorde)

        then:"A model is populated containing the domain instance"
            model.concordeInstance == concorde
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def concorde = new Concorde(params)
            controller.edit(concorde)

        then:"A model is populated containing the domain instance"
            model.concordeInstance == concorde
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/concorde/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def concorde = new Concorde()
            concorde.validate()
            controller.update(concorde)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.concordeInstance == concorde

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            concorde = new Concorde(params).save(flush: true)
            controller.update(concorde)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/concorde/show/$concorde.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/concorde/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def concorde = new Concorde(params).save(flush: true)

        then:"It exists"
            Concorde.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(concorde)

        then:"The instance is deleted"
            Concorde.count() == 0
            response.redirectedUrl == '/concorde/index'
            flash.message != null
    }
}
