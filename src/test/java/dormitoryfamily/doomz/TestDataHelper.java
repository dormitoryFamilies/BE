package dormitoryfamily.doomz;

import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.article.entity.type.BoardType;
import dormitoryfamily.doomz.domain.article.entity.type.StatusType;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.entity.type.CollegeType;
import dormitoryfamily.doomz.domain.member.entity.type.DepartmentType;
import dormitoryfamily.doomz.domain.member.entity.type.GenderType;
import dormitoryfamily.doomz.domain.member.entity.type.MemberDormitoryType;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestDataHelper {

    // 멤머 더미 데이터 생성
    public static Map<String, Member> createDummyMembers() {
        return Map.of(
                "홍길동", createMember(1L, "홍길동", "의적단", "abc@naver.com", "123456789", CollegeType.ENGINEERING, DepartmentType.TELECOMMUNICATION_ENGINEERING, MemberDormitoryType.JINRIGWAN, LocalDate.of(2000, 1, 1), GenderType.MALE, "S3/profile/images", "S3/studentCardImageUrl/images", 0, 0),
                "임꺽정", createMember(2L, "임꺽정", "산적왕", "rim@gmail.com", "987654321", CollegeType.HUMANITIES, DepartmentType.PSYCHOLOGY, MemberDormitoryType.YEJIGWAN, LocalDate.of(1990, 12, 1), GenderType.FEMALE, "S3/profile/images", "S3/studentCardImageUrl/images", 0, 0),
                "이순신", createMember(3L, "이순신", "바다영웅", "hero@daum.net", "1122334455", CollegeType.BIOHEALTH, DepartmentType.BIOHEALTH, MemberDormitoryType.INUIGWAN, LocalDate.of(2000, 5, 5), GenderType.MALE, "S3/profile/images", "S3/studentCardImageUrl/images", 100, 100)
        );
    }

    // 게시글 더미 데이터 생성
    public static List<Article> createDummyArticles(Map<String, Member> members) {
        return Arrays.asList(
                // 본관
                createArticle(1L, members.get("홍길동"), "겨울바다 보고 싶다.", "내용무", "S3/images/abc", StatusType.PROGRESS, ArticleDormitoryType.MAIN_BUILDING, BoardType.HELP, "#방학", 0, 0, 0),
                createArticle(2L, members.get("이순신"), "군대가기 싫다ㅠㅠㅠ", "군대는 왜 가야 하나요?", null, StatusType.DONE, ArticleDormitoryType.MALE_DORMITORY, BoardType.CURIOUS, null, 0, 0, 0),
                createArticle(3L, members.get("임꺽정"), "야~~~호", "메~~~~~~~~~롱", null, StatusType.DONE, ArticleDormitoryType.MALE_DORMITORY, BoardType.CURIOUS, "#장난치기", 0, 0, 0),

                // 양진재
                createArticle(4L, members.get("홍길동"), "기숙사 식구들 화이팅!", "생각보다 오래 걸리는 프로젝트네요! 그래도 화이팅!!", "S3/images/abc", StatusType.PROGRESS, ArticleDormitoryType.FEMALE_DORMITORY, BoardType.HELP, "#바퀴벌래#무서워", 0, 0, 0),
                createArticle(5L, members.get("임꺽정"), "학식 같이 먹을 사람 구해요!", "혼자 먹으니깐 너무 심심해요 ㅠㅠ", "S3/imagesdef", StatusType.DONE, ArticleDormitoryType.FEMALE_DORMITORY, BoardType.CURIOUS, "#학식#저녁#친구", 0, 0, 0),

                // 양성재
                createArticle(6L, members.get("이순신"), "바다 오염이 심각하네요..", "우리 주말에 쓰레기 주우러 갑시다!", null, StatusType.PROGRESS, ArticleDormitoryType.MALE_DORMITORY, BoardType.HELP, "#바다#봉사#주말", 0, 0, 0)
        );
    }

    // 게시글 이미지 더미 데이터 생성
    public static List<ArticleImage> createDummyArticleImages(List<Article> articles) {
        return List.of(
                new ArticleImage(articles.get(0), "S3/studentCardImageUrl/images1"),
                new ArticleImage(articles.get(0), "S3/studentCardImageUrl/images2"),
                new ArticleImage(articles.get(0), "S3/studentCardImageUrl/images3")
        );
    }

    private static Member createMember(Long id, String name, String nickname, String email, String studentNumber, CollegeType collegeType, DepartmentType departmentType, MemberDormitoryType dormitoryType, LocalDate birthDate, GenderType genderType, String profileUrl, String studentCardImageUrl, Integer followerCount, Integer followingCount) {
        Member member = Member.builder()
                .name(name)
                .nickname(nickname)
                .email(email)
                .studentNumber(studentNumber)
                .collegeType(collegeType)
                .departmentType(departmentType)
                .dormitoryType(dormitoryType)
                .birthDate(birthDate)
                .genderType(genderType)
                .profileUrl(profileUrl)
                .studentCardImageUrl(studentCardImageUrl)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .build();
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    private static Article createArticle(Long id, Member member, String title, String content, String thumbnailUrl, StatusType status, ArticleDormitoryType dormitoryType, BoardType boardType, String tags, int commentCount, int viewCount, int wishCount) {
        Article article = Article.builder()
                .member(member)
                .title(title)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .status(status)
                .dormitoryType(dormitoryType)
                .boardType(boardType)
                .tags(tags)
                .commentCount(commentCount)
                .viewCount(viewCount)
                .wishCount(wishCount)
                .build();
        ReflectionTestUtils.setField(article, "id", id);
        return article;
    }
}
