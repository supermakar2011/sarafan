function getIndex(list,id) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id){
            return i;
        }
    }
    return -1;
}
var messageApi = Vue.resource('/message{/id}');
Vue.component('message-form',{
    props:['messages','messageAttr'],
    data:function () {
        return{
            text: '',
            id: ''
        }
    },
    watch:{
        messageAttr:function (newVal,oldVal) {
            this.text = newVal.text;
            this.id = newVal.id;
        }
    },
    template:'<div>' +
        '<input type="text" placeholder="Something text" v-model="text">' +
        '<input type="button" @click="save" value="Save">' +
        '</div>',
    methods:{
        save:function () {
            var message = {text: this.text};
            if (this.id) {
                messageApi.update({id:this.id},message).then(result =>
                result.json().then(data => {
                    var index = getIndex(this.messages,data.id);
                this.messages.splice(index,1,data);
                this.text = '';
                this.id = '';
            }))
            }else{
                messageApi.save({},message).then(result =>
                result.json().then(data => this.messages.push(data)))
                this.text = '';
            }
        }
    }

});
Vue.component('message-row',{
    props: ['message','editMethod','messages','profile'],
    template: '<div>' +
        '<i>({{message.id}})</i>' +
        '<i>{{message.text}}</i>' + '' +
        '<i>{{message.creationDate}}</i>' +
        '<i>Имя пользователя:{{profile.name}}</i>' +
        '<span style="position: absolute;right: 0">' +
        '<input type="button" @click="edit"  value="Edit">' +
        '<input type="button" @click="del"  value="X">' +
        '</span>' +
        '</div>',
    methods:{
        edit:function(){
            this.editMethod(this.message)
        },
        del: function () {
            messageApi.delete({id:this.message.id}).then(result =>{
                if(result.ok){
                this.messages.splice(this.messages.indexOf(this.message,1))
            }
        }
        )
        }
    }
});
Vue.component('messages-list', {
    props: ['messages','profile'],
    data:function(){
        return {
            message: null
        }
    },
    template: '<div style="position: relative;width:1200px" >' +
        '<message-form :messages="messages" :messageAttr="message" />' +
        '<message-row v-for="message in messages" :key="message.id" :message="message" '+
        ':editMethod="editMethod" :messages="messages" :profile="profile"/>' +
        '</div>',

    methods:{
        editMethod:function (message) {
            this.message = message;
        }
    }
});
var app = new Vue({
    el:'#app',
    template: '<div>'+
    '<div v-if="!profile">Необходимо авторизоваться через <a href="/login">Google</a></div>' +
        '<div v-else>' +
            '<div>{{profile.name}}&nbsp<a href="/logout">Выйти</a></div>' +
        '<messages-list  :messages="messages" :profile="profile"/>' +
        '</div>' +
    '</div>',
    data: {
        messages: frontendData.messages,
        profile: frontendData.profile
    },
    created: function () {
        //messageApi.get().then(result =>
       // result.json().then(data =>
        //data.forEach(message => this.messages.push(message))))
    }
});