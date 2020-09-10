package com.tetravalstartups.dingdong.api;

import com.tetravalstartups.dingdong.auth.CheckUserResponse;
import com.tetravalstartups.dingdong.auth.Profile;
import com.tetravalstartups.dingdong.auth.SignUp;
import com.tetravalstartups.dingdong.modules.discover.search.Search;
import com.tetravalstartups.dingdong.modules.player.Seen;
import com.tetravalstartups.dingdong.modules.player.Share;
import com.tetravalstartups.dingdong.modules.profile.external.PublicProfile;
import com.tetravalstartups.dingdong.modules.profile.model.DeleteVideoResponse;
import com.tetravalstartups.dingdong.modules.profile.model.Follow;
import com.tetravalstartups.dingdong.modules.profile.model.followers.Followers;
import com.tetravalstartups.dingdong.modules.profile.model.following.Following;
import com.tetravalstartups.dingdong.modules.profile.videos.created.CreatedVideo;
import com.tetravalstartups.dingdong.modules.profile.videos.liked.LikedVideos;
import com.tetravalstartups.dingdong.modules.publish.UploadVideoResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestInterface {

    @FormUrlEncoded
    @POST("Auth/GetUserData")
    Call<PublicProfile> getUserData(@Field("id") String id, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("User/Uservideos")
    Call<CreatedVideo> getVideoResponse(@Field("id") String id, @Field("requested_user_id") String requested_user_id);

    @FormUrlEncoded
    @POST("User/UserLikeVideos")
    Call<LikedVideos> getLikedVideoResponse(@Field("id") String id);

    @FormUrlEncoded
    @POST("Socialmedia/AllVideoList")
    Call<CreatedVideo> getAllVideos(@Field("user_id") String id, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("Socialmedia/FollowUnfollow")
    Call<Follow> doFollowUser(
            @Field("follower") String follower,
            @Field("following") String following);

    @FormUrlEncoded
    @POST("Socialmedia/MarkVideoSeen")
    Call<Seen> markVideoSeen(
            @Field("id") String id,
            @Field("video_id") String video_id,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("Auth/EditProfile")
    Call<PublicProfile> editUserProfile(@Field("id") String id,
                                  @Field("name") String name,
                                  @Field("email") String email,
                                  @Field("bio") String bio);

    @FormUrlEncoded
    @POST("Auth/EditPhoto")
    Call<PublicProfile> editUserPhoto(@Field("id") String id,
                                @Field("photo") String photo);

    @FormUrlEncoded
    @POST("User/UserFollowers")
    Call<Followers> getUserFollowers(@Field("id") String id);

    @FormUrlEncoded
    @POST("User/UserFollowing")
    Call<Following> getUserFollowing(@Field("id") String id);

    @FormUrlEncoded
    @POST("User/usersearch")
    Call<Search> searchUsers(@Field("user_id") String user_id,
                             @Field("last_index") int last_index,
                             @Field("search") String search);

    @FormUrlEncoded
    @POST("Auth/CheckUser")
    Call<CheckUserResponse> checkUser(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("Socialmedia/deleteVideo")
    Call<DeleteVideoResponse> deleteUserVideo(@Field("id") String id);

    @FormUrlEncoded
    @POST("Socialmedia/ShareVideo")
    Call<Share> shareVideoCount(@Field("video_id") String video_id);

    @FormUrlEncoded
    @POST("Auth/SignUp")
    Call<SignUp> signUpUser(@Field("id") String id,
                            @Field("handle") String handle,
                            @Field("name") String name,
                            @Field("email") String email,
                            @Field("phone") String phone);

    @FormUrlEncoded
    @POST("Socialmedia/UploadVideo")
    Call<UploadVideoResponse> uploadVideo(@Field("id") String id,
                                          @Field("sound_id") String sound_id,
                                          @Field("sound_title") String sound_title,
                                          @Field("user_id") String user_id,
                                          @Field("video_url") String video_url,
                                          @Field("video_thumbnail") String video_thumbnail,
                                          @Field("video_status") int video_status,
                                          @Field("video_desc") String video_desc,
                                          @Field("video_index") String video_index);




}
