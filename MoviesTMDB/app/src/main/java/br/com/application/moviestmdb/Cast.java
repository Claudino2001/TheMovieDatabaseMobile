package br.com.application.moviestmdb;

public class Cast {
    private boolean adult;
    private Integer gender;
    private Integer id;
    private String known_for_department;
    private String name;
    private String original_name;
    private Number popularity;
    private String profile_path;
    private Integer cast_id;
    private String character;
    private String credit_id;
    private Integer order;

    public Cast() {
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKnown_for_department() {
        return known_for_department;
    }

    public void setKnown_for_department(String known_for_department) {
        this.known_for_department = known_for_department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public Number getPopularity() {
        return popularity;
    }

    public void setPopularity(Number popularity) {
        this.popularity = popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public Integer getCast_id() {
        return cast_id;
    }

    public void setCast_id(Integer cast_id) {
        this.cast_id = cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
