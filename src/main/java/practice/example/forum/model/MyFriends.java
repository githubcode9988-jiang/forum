package practice.example.forum.model;

public class MyFriends {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column my_friends.id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column my_friends.my_user_id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    private String myUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column my_friends.my_friend_user_id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    private String myFriendUserId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column my_friends.id
     *
     * @return the value of my_friends.id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column my_friends.id
     *
     * @param id the value for my_friends.id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column my_friends.my_user_id
     *
     * @return the value of my_friends.my_user_id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    public String getMyUserId() {
        return myUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column my_friends.my_user_id
     *
     * @param myUserId the value for my_friends.my_user_id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId == null ? null : myUserId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column my_friends.my_friend_user_id
     *
     * @return the value of my_friends.my_friend_user_id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    public String getMyFriendUserId() {
        return myFriendUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column my_friends.my_friend_user_id
     *
     * @param myFriendUserId the value for my_friends.my_friend_user_id
     *
     * @mbg.generated Mon Feb 14 21:30:39 CST 2022
     */
    public void setMyFriendUserId(String myFriendUserId) {
        this.myFriendUserId = myFriendUserId == null ? null : myFriendUserId.trim();
    }
}