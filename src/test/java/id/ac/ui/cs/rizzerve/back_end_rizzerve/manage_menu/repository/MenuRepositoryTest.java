package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.model.Menu;

@DataJpaTest
public class MenuRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    public void testFindAllByIsActiveOrderByDisplayOrderAsc() {
        Menu activeMenu1 = new Menu();
        activeMenu1.setName("Active Menu 1");
        activeMenu1.setIsActive(true);
        activeMenu1.setDisplayOrder(3);
        entityManager.persist(activeMenu1);

        Menu activeMenu2 = new Menu();
        activeMenu2.setName("Active Menu 2");
        activeMenu2.setIsActive(true);
        activeMenu2.setDisplayOrder(1);
        entityManager.persist(activeMenu2);

        Menu inactiveMenu = new Menu();
        inactiveMenu.setName("Inactive Menu");
        inactiveMenu.setIsActive(false);
        inactiveMenu.setDisplayOrder(2);
        entityManager.persist(inactiveMenu);

        entityManager.flush();

        List<Menu> activeMenus = menuRepository.findAllByIsActiveOrderByDisplayOrderAsc(true);

        assertThat(activeMenus).hasSize(2);
        assertThat(activeMenus.get(0).getName()).isEqualTo("Active Menu 2");
        assertThat(activeMenus.get(1).getName()).isEqualTo("Active Menu 1");
        assertThat(activeMenus).extracting("displayOrder").containsExactly(1, 3);
    }
}