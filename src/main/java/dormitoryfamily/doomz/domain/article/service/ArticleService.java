package dormitoryfamily.doomz.domain.article.service;

import dormitoryfamily.doomz.domain.article.dto.request.CreateArticleRequestDto;
import dormitoryfamily.doomz.domain.article.dto.response.ArticleResponseDto;
import dormitoryfamily.doomz.domain.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.article.exception.ArticleNotExistsException;
import dormitoryfamily.doomz.domain.article.repository.ArticleImageRepository;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    @Transactional
    public CreateArticleResponseDto save(Member member, CreateArticleRequestDto requestDto) {
        Article article = CreateArticleRequestDto.toEntity(member, requestDto);

        articleRepository.save(article);
        saveArticleImages(article, requestDto);

        return CreateArticleResponseDto.fromEntity(article);
    }

    private void saveArticleImages(Article article, CreateArticleRequestDto requestDto) {
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
}
