# PROMPTS.md - Laboratorio 06 (Fase 2: mejora con IA)

**Herramienta:** agente de Android Studio (Gemini, Agent mode)
**Mejora obligatoria:** badge con contador en el ítem "Favoritos" del drawer, conectado a la opción "Favoritos" del DropdownMenu de cada producto.

## Prompt único (diseño de referencia + badge + datos del alumno)

**Prompt:**
Estoy trabajando en una app Jetpack Compose (Kotlin, Material 3) llamada TECSUP Store, paquete com.escobal.tecsupstore. Archivos: MainActivity.kt, Producto.kt, DatosTienda.kt, PantallaInicio.kt, PantallaSimple.kt, TarjetaProducto.kt, AppDrawer.kt, AppNavegacion.kt y ui/theme (Color.kt, Theme.kt). La app ya tiene un LazyRow de categorías, un LazyColumn de productos, un DropdownMenu en cada tarjeta, un ModalNavigationDrawer y navegación con NavHost.

Haz todos estos cambios juntos para que la app se vea como un diseño de referencia morado. Si algo de lo siguiente ya existe, ajústalo sin duplicarlo. No agregues librerías ni ViewModel y que el proyecto compile.

TEMA (ui/theme)

Usa un esquema de colores morado fijo, sin dynamic color: primary 0xFF5B2D8E, onPrimary blanco, primaryContainer 0xFFEDE4F7, onPrimaryContainer 0xFF3E1F68, surface blanco. Aplica ese esquema en TecsupStoreTheme (claro).

TOPBAR (AppNavegacion.kt)

Reemplaza la barra superior por una TopAppBar alineada a la izquierda con fondo primary y contenido onPrimary. El título es un Column con "TECSUP Store" en negrita (titleLarge) y debajo "ESCOBAL" en bodySmall. Mantén el ícono ☰ a la izquierda, que abre el drawer.

PRODUCTOS (DatosTienda.kt, PantallaInicio.kt, TarjetaProducto.kt)

Agrega a la lista de productos: "Audífonos" S/ 89.00, "Smartwatch" S/ 199.00 y "Funda celular" S/ 25.00 (ids nuevos, categorías existentes o nuevas), conservando los demás.
Muestra el precio con formato "S/ 89.00" (dos decimales).
Cada TarjetaProducto es una Card de esquinas redondeadas (16.dp) con containerColor = Color(0xFFF3EDF9), sin sombra marcada. Dentro, de izquierda a derecha: un recuadro redondeado (56.dp, color primaryContainer) con un ícono Icons.Default.ShoppingBag o ShoppingCart en color primary; un Column con el nombre en negrita y debajo el precio en color primary; y a la derecha el ícono ⋮ (MoreVert) dentro de un círculo blanco pequeño.
Cuando el menú de la tarjeta está abierto, la tarjeta muestra un borde de 2.dp en color primary.
El LazyRow de categorías y la agrupación por secciones se mantienen.

DROPDOWNMENU (TarjetaProducto.kt)

El DropdownMenu va dentro de un Box junto al ícono ⋮, con fondo blanco y esquinas redondeadas, con 3 opciones separadas por HorizontalDivider: "Favoritos", "Compartir" y "Reportar", cada una con su leadingIcon (corazón, Icons.Default.Share y Icons.Default.Warning).
La opción Favoritos usa los parámetros esFavorito: Boolean y onToggleFavorito: (Int) -> Unit: si esFavorito es false muestra "Agregar a favoritos" con Icons.Default.FavoriteBorder; si es true muestra "Quitar de favoritos" con Icons.Default.Favorite en color primary. Al hacer clic llama a onToggleFavorito(producto.id) y cierra el menú.
Firma: TarjetaProducto(producto: Producto, esFavorito: Boolean, onToggleFavorito: (Int) -> Unit).

ESTADO DE FAVORITOS (state hoisting, AppNavegacion.kt)

Crea val favoritos = remember { mutableStateListOf<Int>() } con los ids favoritos y una función onToggleFavorito: (Int) -> Unit que agrega el id si no está y lo quita si ya está. Pasa favoritos y onToggleFavorito del NavHost a PantallaInicio(favoritos: List<Int>, onToggleFavorito: (Int) -> Unit) y de ahí a TarjetaProducto, calculando esFavorito = producto.id in favoritos.

DRAWER (AppDrawer.kt)

El ModalDrawerSheet tiene esquinas redondeadas a la derecha y fondo blanco.
Encabezado del usuario: un avatar circular (56.dp, fondo primaryContainer) con las iniciales "NE" en negrita color primary; al lado el nombre "Noe Oswaldo Escobal" en negrita y debajo el correo "noe.escobal@tecsup.edu.pe" en bodySmall. Debajo del encabezado va un HorizontalDivider.
Ítems, en este orden: Inicio, Mis pedidos, Favoritos, Perfil y Cerrar sesión, cada uno con su ícono. El ítem activo (según la ruta actual) se resalta con selectedContainerColor = primaryContainer y texto en negrita color onPrimaryContainer. Cerrar sesión no navega.
Agrega el parámetro cantidadFavoritos: Int. En el ítem "Favoritos" usa el parámetro badge: si cantidadFavoritos > 0 muestra un Badge de Material 3 con el número; si es 0, no muestra nada. Los demás ítems no llevan badge. En AppNavegacion.kt pasa cantidadFavoritos = favoritos.size.
El badge debe actualizarse solo al agregar o quitar un favorito desde el menú ⋮.
**Respuesta resumida:** El agente modificó el tema (colores morados), DatosTienda.kt (nuevos productos), TarjetaProducto.kt (diseño de tarjeta, borde con el menú abierto, DropdownMenu con divisores y opción Favoritos que alterna), PantallaInicio.kt (recibe y pasa los favoritos), AppDrawer.kt (encabezado con avatar, ítem activo resaltado y badge con contador) y AppNavegacion.kt (TopAppBar con "TECSUP Store" y "ESCOBAL", estado elevado de favoritos).

**Qué corregí:** COMPLETAR (escribe aquí lo que realmente cambiaste a mano; por ejemplo imports faltantes, un ícono que no existía, o la lista de favoritos si no recomponía).
