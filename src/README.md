## Tema 2 APD

### Implementare Dispatcher

* Se creeaza o lista pentru a tine evidenta task-urilor
* In functie de algoritm se face un switch-case cu fiecare caz:
    * Round Robin: Task-ul se trimite la host-ul cu id-ul restului impartirii indicelui noului task cu numarul de host-uri
    * Shortest Queue: Se itereaza prin host-uri cautand host-ul cu numarul minim de task-uri si se adauga acestuia
    * Size Interval Task Assignment: Se mai aplica un switch-case pentru cele trei posibilitati: scurt, mediu sau lung
    * Least Work Left: Se itereaza prin host-uri cautand host-ul cu timpul minim ramas de executat si se adauga task-ul in acesta

### Implementare Host
* Se creaeza o lista pe care folosita ca o coada
* Se creaeza un obiect pentru a fi folosit in sincronizare
* Se creeaza un boolean pentru a fi folosit pentru a oprii executia
* Se creeaza in numar care tine cont cate secunde mai sunt din task-ul curent
* In executie se foloseste un task local pentru a tine cont care este task-ul curent
* Se sorteaza la inceput de executie lista in functie de prioritate(descrescator), si de timpul de start(crescator)
* Daca exista un task curent se extrage primul din lista, daca exista elemente
* Pentru a simula executia se foloseste sleep in thread si se modifica timpul ramas task-ului ramas
* In cazul in care task-ul e preemptibil se verifica daca exista in lista un task cu prioritate mai mare

### Medode de sincronizare folosite
* Collections.synchronized
* Atomic numbers
