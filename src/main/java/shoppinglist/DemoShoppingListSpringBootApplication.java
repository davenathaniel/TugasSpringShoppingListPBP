package shoppinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shoppinglist.controller.Controller;
import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaDetilRepo;
import shoppinglist.repository.DaftarBelanjaRepo;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;


@SpringBootApplication
public class DemoShoppingListSpringBootApplication implements CommandLineRunner
{
    @Autowired
    private DaftarBelanjaRepo repo;
    @Autowired
    private DaftarBelanjaDetilRepo repoDetil;
    private Scanner keyb = new Scanner(System.in);

    public static void main(String[] args)
    {
        SpringApplication.run(DemoShoppingListSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        System.out.println("Membaca Semua Record DaftarBelanja:");
        List<DaftarBelanja> all = repo.findAll();
        for (DaftarBelanja db : all) {
            System.out.println("[" + db.getId() + "] " + db.getJudul());

            List<DaftarBelanjaDetil> listBarang = db.getDaftarBarang();
            for (DaftarBelanjaDetil barang : listBarang) {
                System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
            }
        }

        // Baca berdasarkan ID
        System.out.print("Masukkan ID dari objek DaftarBelanja yang ingin ditampilkan: ");
        long id = Long.parseLong(keyb.nextLine());
        System.out.println("Hasil pencarian: ");
        
        Optional<DaftarBelanja> optDB = repo.findById(id);
        if (optDB.isPresent()) {
            DaftarBelanja db = optDB.get();
            System.out.println("\tJudul: " + db.getJudul());
        }
        else {
            System.out.println("\tTIDAK DITEMUKAN.");
        }
        Controller.cariDaftarBelanja(repo, keyb);
        Controller.tambahDaftarBelanja(repo, keyb);
        Controller.hapusDaftarBelanja(repo, repoDetil, keyb);
        Controller.perbaharuiDaftarBelanja(repo, keyb);
    }
}
