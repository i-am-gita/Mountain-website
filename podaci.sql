INSERT INTO `Termin_znamenitost` (`idTermin`,`pocetak`,`kraj`,`Znamenitost_idZnamenitost`)
VALUES ('1','2020-01-30 10:00:00','2020-01-30 12:00:00','1'),('2','2020-01-30 13:00:00','2020-01-30 15:00:00','1'),('3','2020-02-15 10:00:00','2020-02-15 12:00:00','2'),('4','2020-01-30 13:00:00','2020-01-30 15:00:00','2'),('5','2020-01-30 13:00:00','2020-01-30 15:00:00','3')


INSERT INTO `Znamenitost` (`idZnamenitost`,`opis`,`tip`,`Planinarska_staza_idStaza`)
VALUES ('1','Sagradjena u 12. veku','Crkva','1'),('2','Tvorevina Dusana silnog','Tvrdjava','2'),('3','Staniste velikog broja riba','Jezero','3'),('4','Voda koja leci','Vodopad','4'),('5','Nekada puna zlata i dijamanata, sada turisticka atrakcija','Pecina','5')


INSERT INTO `Planinarska_staza` (`idStaza`,`naziv`,`tezina`,`mapa`,`opis`,`Planina_idPlanina`) VALUES (1,'Dolina smrti',10,load_file('/home/gitta/Desktop/PlaninarskoDruštvo/slike/mapa1.png'),'Sa ove staze mnogi su pali, samo im jos jedan fali.',1);
INSERT INTO `Planinarska_staza` (`idStaza`,`naziv`,`tezina`,`mapa`,`opis`,`Planina_idPlanina`) VALUES (2,'Deciji krug',1,load_file('/home/gitta/Desktop/PlaninarskoDruštvo/slike/mapa2.png'),'Staza za pocetnike, ukoliko prvi put skijate ovo je staza za Vas.',1);
INSERT INTO `Planinarska_staza` (`idStaza`,`naziv`,`tezina`,`mapa`,`opis`,`Planina_idPlanina`) VALUES (3,'Umri muski',8,load_file('/home/gitta/Desktop/PlaninarskoDruštvo/slike/mapa3.png'),'Staza za najiskusnije skijase. Padovi su nenormalni.',1);
INSERT INTO `Planinarska_staza` (`idStaza`,`naziv`,`tezina`,`mapa`,`opis`,`Planina_idPlanina`) VALUES (4,'Skoci ili koci',5,load_file('/home/gitta/Desktop/PlaninarskoDruštvo/slike/mapa1.png'),'Zanimljiva staza sa mnogo znamenitosti kojima mozete pristupiti. Obratite paznju jer za neke znamenitosti je potrebno zakazati termin',2);
INSERT INTO `Planinarska_staza` (`idStaza`,`naziv`,`tezina`,`mapa`,`opis`,`Planina_idPlanina`) VALUES (5,'Krdo niz brdo',2,load_file('/home/gitta/Desktop/PlaninarskoDruštvo/slike/mapa2.png'),'Najposecenija staza na ovoj planini. Bogata raznim znamenitostima koje mozete uvek obici.',2);

INSERT INTO Dom (idDom, naziv, max_kapacitet, Planina_idPlanina)
VALUES (1, 'Pentranje', 5, 1),(2, 'Mecava', 10, 2),(3, 'Lobe', 50, 2),(4, 'Lux', 100, 3)

INSERT INTO Planina (idPlanina, naziv)
VALUES (1,'Zlatibor'),(2,'Tara'),(3,'Kopaonik'),(4,'Stara planina'),(5,'Rtanj'),(6,'Golija'),(7,'Avala')

INSERT INTO Uloge (idUloga, naziv)
VALUES (1,'clan'),(2,'sekretar')
