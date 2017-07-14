package com.example.daniel.videostreaming.models;

import android.content.Context;

import com.example.daniel.videostreaming.utils.LibraryClass;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 02/07/17.
 */

//@JsonIgnoreProperties({"id", "senha"})
public class User {

    public static String PROVIDER = "PROVIDER";
    public static String TOKEN = "TOKEN";
    private static User user;

    private String nome;
    private String email;
    private String senha;
    private String escolaridade;
    private String idade;
    private String universidade_escola;
    private String curso_serie;
    private String materia_domoinio;
    private String materia_dificuldade;
    private String id;
    private String id_foto;
    private List<String> lista_chat;
    private String latitude;
    private String longetude;


    public User() {
        //this.lista_chat = new ArrayList<>();
    }

    public User(String nome, String email, String escolaridade, String idade, String materia_domoinio, String materia_dificuldade, String id) {
        this.nome = nome;
        this.email = email;
        this.escolaridade = escolaridade;
        this.idade = idade;
        this.materia_domoinio = materia_domoinio;
        this.materia_dificuldade = materia_dificuldade;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private void setNameInMap( Map<String, Object> map ) {
        if( getNome() != null ){
            map.put( "nome", getNome() );
        }
    }

    public void setNameIfNull(String name) {
        if( this.nome == null ){
            this.nome = name;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private void setEmailInMap( Map<String, Object> map ) {
        if( getEmail() != null ){
            map.put( "email", getEmail() );
        }
    }

    public void setEmailIfNull(String email) {
        if( this.email == null ){
            this.email = email;
        }

    }

    //@Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getUniversidade_escola() {
        return universidade_escola;
    }

    public void setUniversidade_escola(String universidade_escola) {
        this.universidade_escola = universidade_escola;
    }

    public String getCurso_serie() {
        return curso_serie;
    }

    public void setCurso_serie(String curso_serie) {
        this.curso_serie = curso_serie;
    }

    public String getMateria_domoinio() {
        return materia_domoinio;
    }

    public void setMateria_domoinio(String materia_domoinio) {
        this.materia_domoinio = materia_domoinio;
    }

    public String getMateria_dificuldade() {
        return materia_dificuldade;
    }

    public void setMateria_dificuldade(String materia_dificuldade) {
        this.materia_dificuldade = materia_dificuldade;
    }

    //@Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_foto() {
        return id_foto;
    }

    public void setId_foto(String id_foto) {
        this.id_foto = id_foto;
    }


    public List<String> getLista_chat() {
        return lista_chat;
    }

    public void setLista_chat(List<String> lista_chat) {
        this.lista_chat = lista_chat;
    }

    public void clearIdsPosts(){
        this.lista_chat.clear();
    }

    public void addIdChat(String chat){
        //this.lista_chat = new ArrayList<>();
        if(!this.lista_chat.contains(chat)){
            this.lista_chat.add(chat);
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", escolaridade='" + escolaridade + '\'' +
                ", idade='" + idade + '\'' +
                ", universidade_escola='" + universidade_escola + '\'' +
                ", curso_serie='" + curso_serie + '\'' +
                ", materia_domoinio='" + materia_domoinio + '\'' +
                ", materia_dificuldade='" + materia_dificuldade + '\'' +
                '}';
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongetude() {
        return longetude;
    }

    public void setLongetude(String longetude) {
        this.longetude = longetude;
    }


    public String getTokenSP(Context context ){
        String token = LibraryClass.getSP( context, TOKEN );
        return( token );
    }

    public void saveTokenSP(Context context, String token ){
        LibraryClass.saveSP( context, TOKEN, token );
    }


    /*
    public void saveDB(){
        Firebase firebase = LibraryClass.getFirebase();
        firebase = firebase.child("users").child( getId() );
        firebase.setValue( this );
    }

    public void updateListaChat(){
        Firebase firebase =  LibraryClass.getFirebase();
        firebase = firebase.child("users").child( getId() );

        Map<String, Object > map = new HashMap<>();
        map.put("lista_chat", getLista_chat());

        firebase.updateChildren(map);
    }
    */

}
