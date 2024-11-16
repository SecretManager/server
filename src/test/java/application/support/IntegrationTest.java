package application.support;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;

import application.encrypt.application.EncryptService;
import application.encrypt.persistence.metadata.FileMetadataEntityRepository;
import application.infra.s3.S3ApiClient;
import application.member.application.MemberService;
import application.member.persistence.MemberEntity;
import application.member.persistence.MemberEntityRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@ExtendWith(DataClearExtension.class)
@SpringBootTest
public abstract class IntegrationTest extends MockTestSupport {

    @MockBean
    protected S3ApiClient s3ApiClient;

    @Autowired
    protected MemberEntityRepository memberEntityRepository;

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected EncryptService encryptService;

    @Autowired
    protected FileMetadataEntityRepository fileMetadataEntityRepository;

    protected MemberEntity createMember(Long id) {
        return sut.giveMeBuilder(MemberEntity.class)
                .set(javaGetter(MemberEntity::getId), id)
                .sample();
    }
}
