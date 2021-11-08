package io.mustelidae.smallclawedotter.api.domain.board

import io.kotest.matchers.shouldBe
import io.mustelidae.smallclawedotter.utils.invokeId
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class ArticleTest {

    @Test
    fun onHiddenTest() {
        val article = Article.aFixture(true)

        article.onHidden(LocalDateTime.now().plusDays(1))

        article.isHidden() shouldBe true

        article.onHidden(LocalDateTime.now().minusDays(1))

        article.isHidden() shouldBe false

        article.offHidden()

        article.isHidden() shouldBe false
    }
}

internal fun Article.Companion.aFixture(hasId: Boolean = false): Article {
    val article = Article(
        "test notice",
        "welcome!"
    ).apply {
        invokeId(this, hasId)
    }

    article.setBy(
        Paragraph(
            Paragraph.Type.MARKDOWN,
            """
                ---
                __Advertisement :)__

                - __[pica](https://nodeca.github.io/pica/demo/)__ - high quality and fast image
                  resize in browser.
                - __[babelfish](https://github.com/nodeca/babelfish/)__ - developer friendly
                  i18n with plurals support and easy syntax.

                You will like those projects!

                ---

                # h1 Heading 8-)
                ## h2 Heading
                ### h3 Heading
                #### h4 Heading
                ##### h5 Heading
                ###### h6 Heading


                ## Horizontal Rules

                ___

                ---

                ***


                ## Typographic replacements

                Enable typographer option to see result.

                (c) (C) (r) (R) (tm) (TM) (p) (P) +-

                test.. test... test..... test?..... test!....

                !!!!!! ???? ,,  -- ---

                "Smartypants, double quotes" and 'single quotes'


                ## Emphasis

                **This is bold text**

                __This is bold text__

                *This is italic text*

                _This is italic text_

                ~~Strikethrough~~


                ## Blockquotes


                > Blockquotes can also be nested...
                >> ...by using additional greater-than signs right next to each other...
                > > > ...or with spaces between arrows.


                ## Lists

                Unordered

                + Create a list by starting a line with `+`, `-`, or `*`
                + Sub-lists are made by indenting 2 spaces:
                  - Marker character change forces new list start:
                    * Ac tristique libero volutpat at
                    + Facilisis in pretium nisl aliquet
                    - Nulla volutpat aliquam velit
                + Very easy!

                Ordered
            """.trimIndent()
        )
    )

    article.addBy(
        Attachment(
            Attachment.Type.IMAGE,
            1,
            "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png"
        )
    )

    return article
}
