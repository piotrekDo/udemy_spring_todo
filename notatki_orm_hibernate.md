# ORM i Hibernate

Hibernate do działania wykorzystuje *EntityManager* do komunikacji z bazą danych.

### Adnotacja @Column




### Adnotacja @Transactional

Transactional pozwala na wprowadzenie do bazy danych zmian na zarządanym przez Hibernate obiekcie. Nie musimy zapisywać
w kodzie dodatkowych metod na zapis zmian. Transakcja zostaje przerwana w momencie wystąpienia wyjątku obsługiwanego.
Zachowaniem tym można manipulować poprzez [dodatkowe ustawienia](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-attransactional-settings)&#x00B9;
możemy na przykład zdefiniować rodzaj wyjątków przerywających działanie transakcji poprzez atrybut *rollbackFor*.
**Metoda na której wykonujemy @Transactional musi być publiczna i musi być częścią Springowego *Bean'a* a także musi
zostać wywołana z innego *bean'a* Springowego**



<br></br>
<br></br>
<br></br>
### Linki
&#x00B9; Dodatkowe ustawienia dla adnotacji @Transactional  
https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-attransactional-settings