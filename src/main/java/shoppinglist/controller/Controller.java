/*
 * Controller.java
 *
 * Created on Mar 26, 2021, 20.22
 */
package shoppinglist.controller;

import shoppinglist.entity.DaftarBelanja;
import shoppinglist.entity.DaftarBelanjaDetil;
import shoppinglist.repository.DaftarBelanjaDetilRepo;
import shoppinglist.repository.DaftarBelanjaRepo;

import javax.xml.bind.SchemaOutputResolver;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


/**
 * @author dave
 */

public class Controller {
    public static void cariDaftarBelanja(DaftarBelanjaRepo repo, Scanner keyb) {
        System.out.print("Masukkan Nama dari objek DaftarBelanja yang ingin ditampilkan: ");
        String judul = keyb.nextLine();
        System.out.println("Hasil pencarian: ");
        List<DaftarBelanja> allByJudul = repo.findByJudulIgnoreCaseContaining(judul);
        if(!allByJudul.isEmpty()) {
            for (DaftarBelanja db : allByJudul) {
                System.out.println("\t"+db.getJudul());
                List<DaftarBelanjaDetil> listBarang = db.getDaftarBarang();
                for (DaftarBelanjaDetil barang : listBarang) {
                    System.out.println("\t" + barang.getNamaBarang() + " " + barang.getByk() + " " + barang.getSatuan());
                }
            }
        } else {
            System.out.println("\tDATA TIDAK DITEMUKAN");
        }
    }

    public static void tambahDaftarBelanja(DaftarBelanjaRepo repo, Scanner keyb) {
        System.out.print("Input Judul yang akan ditambahkan(input 0 untuk batal): ");
        String judul = keyb.nextLine();
        if(!judul.equals("0")) {
            DaftarBelanja daftarBelanja = new DaftarBelanja();
            daftarBelanja.setJudul(judul);
            daftarBelanja.setTanggal(LocalDateTime.now());
            repo.save(daftarBelanja);
            System.out.println("Input jumlah detil daftar belanja: ");
            int jml = keyb.nextInt();
            keyb.nextLine();
            List<DaftarBelanjaDetil> listBelanja = new LinkedList<>();
            for(int i=0;i<jml;i++) {
                DaftarBelanjaDetil detil = new DaftarBelanjaDetil();
                detil.setId(daftarBelanja.getId(),i + 1);
                System.out.print("Input nama barang: ");
                String nama = keyb.nextLine();
                detil.setNamaBarang(nama);
                System.out.print("Input jumlah barang: ");
                long banyak = Long.parseLong(keyb.nextLine());
                detil.setByk(banyak);
                System.out.print("Input satuan: ");
                String satuan = keyb.nextLine();
                detil.setSatuan(satuan);
                System.out.print("Input memo(catatan): ");
                String memo = keyb.nextLine();
                detil.setMemo(memo);
                listBelanja.add(detil);
            }
            daftarBelanja.setDaftarBarang(listBelanja);
            repo.save(daftarBelanja);
            System.out.println("Judul: " + judul);
            for(int i=0;i<listBelanja.size();i++) {
                System.out.println("\t[" + (i+1) + "]");
                System.out.println("\t\tNama Barang: " + listBelanja.get(i).getNamaBarang());
                System.out.println("\t\tJumlah: " + listBelanja.get(i).getByk() + " " +listBelanja.get(i).getSatuan());
                System.out.println("\t\tMemo: " + listBelanja.get(i).getMemo());
            }
            System.out.println("\tMenambahkan daftar belanja berhasil");
        } else {
            System.out.println("\tMenambahkan daftar belanja dibatalkan");
        }
    }

    public static void hapusDaftarBelanja(DaftarBelanjaRepo repo, DaftarBelanjaDetilRepo repoDetil, Scanner keyb) {
        System.out.print("Input ID daftar belanja yang akan dihapus(input 0 untuk batal): ");
        long id = Long.parseLong(keyb.nextLine());
        if(id != 0) {
            Optional<DaftarBelanja> optDB = repo.findById(id);
            if (optDB.isPresent()) {
                DaftarBelanja daftarBelanja = optDB.get();
                System.out.println("Daftar belanja dengan judul " + repo.findById(id).get().getJudul() + " telah dihapus");
                repoDetil.deleteByDaftarBelanja_id(daftarBelanja.getId());
                repo.deleteById(id);
            } else {
                System.out.println("\tTIDAK DITEMUKAN.");
            }
        } else {
            System.out.println("Hapus daftar belanja dibatalkan");
        }
    }

    public static void perbaharuiDaftarBelanja(DaftarBelanjaRepo repo, Scanner keyb) {
        System.out.print("Input ID daftar belanja yang akan diperbaharui(input 0 untuk batal): ");
        long id = Long.parseLong(keyb.nextLine());
        if(id != 0) {
            Optional<DaftarBelanja> optDb = repo.findById(id);
            if(optDb.isPresent()) {
                DaftarBelanja daftarBelanja = optDb.get();
                System.out.println("Judul: " + daftarBelanja.getJudul());
                System.out.print("Input judul baru: ");
                String judul = keyb.nextLine();
                daftarBelanja.setJudul(judul);
                daftarBelanja.setTanggal(LocalDateTime.now());
                repo.save(daftarBelanja);
                System.out.println("Daftar belanja telah berhasil diperbaharui");
                while(true) {
                    System.out.print("Masukkan ID barang yang diperbaharui(input 0 untuk batal): ");
                    List<DaftarBelanjaDetil> listBarang = daftarBelanja.getDaftarBarang();
                    int noUrut = Integer.parseInt(keyb.nextLine());
                    if (noUrut == 0) {
                        System.out.println("Ubah daftar belanja detil dibatalkan");
                        break;
                    } else {
                        DaftarBelanjaDetil barang = daftarBelanja.getBarang(noUrut);
                        System.out.println("Data baru: ");
                        System.out.print("Nama barang: ");
                        String nama = keyb.nextLine().trim();
                        System.out.print("jumlah : ");
                        long jumlah = keyb.nextInt();
                        keyb.nextLine();
                        System.out.print("Satuan: ");
                        String satuan = keyb.nextLine();
                        System.out.print("Memo : ");
                        String memo = keyb.nextLine().trim();
                        barang.setNamaBarang(nama);
                        barang.setByk(jumlah);
                        barang.setSatuan(satuan);
                        barang.setMemo(memo);
                        daftarBelanja.setBarang(barang, noUrut);
                        repo.save(daftarBelanja);
                    }
                }
            } else {
                System.out.println("Daftar belanja tidak ditemukan");
            }
        } else {
            System.out.println("Perbarui data dibatalkan");
        }
    }
}
