package dormitoryfamily.doomz.domain.dormitory.entity;

import jakarta.persistence.*;

@Entity
public class Dormitory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dormitory_id")
    private Long id;
}
