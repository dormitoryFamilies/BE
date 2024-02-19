package dormitoryfamily.doomz.domain.article.service;

import dormitoryfamily.doomz.domain.article.dto.request.ArticleRequestDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleImageRepository;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.InvalidMemberAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    @Transactional
    public CreateArticleResponseDto save(Member member, ArticleRequestDto requestDto) {
        Article article = ArticleRequestDto.toEntity(member, requestDto);

        articleRepository.save(article);
        saveArticleImages(article, requestDto);

        return CreateArticleResponseDto.fromEntity(article);
    }

    private void saveArticleImages(Article article, ArticleRequestDto requestDto) {
        if (!requestDto.imagesUrls().isEmpty()) {
            requestDto.imagesUrls().forEach(url -> {
                ArticleImage articleImage = ArticleImage.builder().article(article).imageUrl(url).build();
                articleImageRepository.save(articleImage);
            });
        }
    }

    @Transactional
    public ArticleResponseDto findArticle(Member member, Long articleId) {
        Article article = getArticleById(articleId);
        List<ArticleImage> articleImages = articleImageRepository.findByArticleId(articleId);

        article.plusViewCount();
        return ArticleResponseDto.fromEntity(member, article, articleImages);
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotExistsException::new);
    }

    @Transactional
    public void updateArticle(Member member, Long articleId, ArticleRequestDto requestDto) {
        Article article = getArticleById(articleId);
        isWriter(member, article.getMember());

        articleImageRepository.deleteAllByArticle(article);
        saveArticleImages(article, requestDto);

        article.updateArticle(requestDto);
    }

    private void isWriter(Member loginMember, Member writer) {
        if (!Objects.equals(loginMember.getId(), writer.getId())) {
            throw new InvalidMemberAccessException();
        }
    }

    public void deleteArticle(Member loginMember, Long articleId) {
        Article article = getArticleById(articleId);
        isWriter(loginMember, article.getMember());

        articleRepository.delete(article);
    }
}
