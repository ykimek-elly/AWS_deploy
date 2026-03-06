package com.yonsai.springAi2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name ="produts")
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;    //상품명
	private String price;   //가격(일단 문자로)
	private String feature; //특징

}
