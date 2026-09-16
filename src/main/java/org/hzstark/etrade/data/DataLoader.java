package org.hzstark.etrade.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    
    public DataLoader(ProductRepository productRepository, AdminRepository adminRepository) {
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Init Admin if not exists
        if (adminRepository.findByUsername("admin") == null) {
            adminRepository.save(new AdminEntity("admin", "admin123"));
        }

        // Initialize 200 products if DB is empty
        if (productRepository.count() == 0) {
            List<ProductEntity> products = new ArrayList<>();
            String[] categories = {"Elektronik", "Giyim", "Ev & Yaşam", "Spor & Outdoor", "Kitap"};
            Random random = new Random();

            for (int i = 1; i <= 200; i++) {
                String category = categories[random.nextInt(categories.length)];
                String name = category + " Ürünü Model " + (1000 + i);
                String desc = "Bu ürün " + category + " kategorisinde yer alan yüksek kaliteli bir seçenektir. " +
                              "Mükemmel performansı ve uzun ömürlü yapısı ile dikkat çeker. Gelişmiş özellikleri sayesinde tüm ihtiyaçlarınızı karşılar.";
                BigDecimal price = BigDecimal.valueOf(100 + random.nextInt(9900) + random.nextDouble()).setScale(2, java.math.RoundingMode.HALF_UP);
                Integer stock = random.nextInt(500) + 1; // 1 to 500 stock
                
                String imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=300&auto=format&fit=crop";
                
                switch(category) {
                    case "Elektronik": imageUrl = "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=300&auto=format&fit=crop"; break;
                    case "Giyim": imageUrl = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=300&auto=format&fit=crop"; break;
                    case "Ev & Yaşam": imageUrl = "https://images.unsplash.com/photo-1484101403633-562f891dc89a?w=300&auto=format&fit=crop"; break;
                    case "Spor & Outdoor": imageUrl = "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=300&auto=format&fit=crop"; break;
                    case "Kitap": imageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=300&auto=format&fit=crop"; break;
                }

                products.add(new ProductEntity(name, category, desc, price, stock, imageUrl));
            }
            productRepository.saveAll(products);
        }
    }
}
