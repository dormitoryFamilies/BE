package dormitoryfamily.doomz.domain.article.service;

import dormitoryfamily.doomz.domain.article.dto.request.CreateArticleRequestDto;
import dormitoryfamily.doomz.domain.article.dto.response.CreateArticleResponseDto;
import dormitoryfamily.doomz.domain.article.entity.Article;
import dormitoryfamily.doomz.domain.article.entity.ArticleImage;
import dormitoryfamily.doomz.domain.article.repository.ArticleImageRepository;
import dormitoryfamily.doomz.domain.article.repository.ArticleRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    public CreateArticleResponseDto save(Member member, CreateArticleRequestDto requestDto) {
        Article article = CreateArticleRequestDto.toEntity(member, requestDto);

        articleRepository.save(article);
        saveArticleImages(article, requestDto);

        return CreateArticleResponseDto.fromEntity(article);
    }

    private void saveArticleImages(Article article, CreateArticleRequestDto requestDto) {
        requestDto.imagesUrls().forEach(url -> {
                    ArticleImage articleImage = ArticleImage.builder().article(article).imageUrl(url).build();
                    articleImageRepository.save(articleImage);
                }
        );
    }

}
