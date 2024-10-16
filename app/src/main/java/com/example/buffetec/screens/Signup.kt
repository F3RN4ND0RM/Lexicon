

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }

    // Ciudades y colonias específicas
    val cities = listOf("Monterrey", "San Pedro Garza García", "Apodaca", "San Nicolás de los Garza", "Guadalupe", "Escobedo", "Santa Catarina")
    val neighborhoodsByCity = mapOf(
        "Monterrey" to listOf("Tecnológico", "Mitras", "San Jerónimo", "Cumbres", "Centro"),
        "San Pedro Garza García" to listOf("Valle Oriente", "Del Valle", "San Agustín", "Chipinque", "Fuentes del Valle"),
        "Apodaca" to listOf("Cumbres", "Linda Vista", "La Fé", "Santa Rosa", "Pueblo Nuevo"),
        "San Nicolás de los Garza" to listOf("Anáhuac", "Residencial Las Puentes", "La Estancia", "Los Álamos", "Hacienda Los Morales"),
        "Guadalupe" to listOf("Contry", "Las Quintas", "Valle de Guadalupe", "Jardines de Linda Vista", "Camino Real"),
        "Escobedo" to listOf("Privadas de Anáhuac", "Las Torres", "San Miguel Residencial", "Villas de Anáhuac", "Monterreal"),
        "Santa Catarina" to listOf("La Fama", "El Lechugal", "Residencial Santa Catarina", "Infonavit La Huasteca", "Puerta Mitras")
    )

    // Control de selección de ciudad y colonia
    var selectedCity by remember { mutableStateOf("Ingresa una opción") }
    var selectedNeighborhood by remember { mutableStateOf("Ingresa una opción") }
    var cityExpanded by remember { mutableStateOf(false) }
    var neighborhoodExpanded by remember { mutableStateOf(false) }

    val neighborhoods = neighborhoodsByCity[selectedCity] ?: listOf("Ingresa una opción")


    val context = LocalContext.current
    val db = UserDatabase.getDatabase(context).userDao()
    val coroutineScope = rememberCoroutineScope()

    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Validación de correo electrónico
    val emailPattern = Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6200EA)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .background(
                    Color.White,
                    shape = RoundedCornerShape(20.dp)
                ) // Fondo blanco con esquinas redondeadas
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Regresar",
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.ExtraLight,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick ={
                        navController.navigate(Screen.Login.route)
                    })
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la empresa",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )


            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it.capitalize() },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Apellido
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it.capitalize() },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Correo Electrónico con validación
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                isError = !emailPattern.matcher(email).matches(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña con iconos de visibilidad
            OutlinedTextField(
                value = pwd,
                onValueChange = { pwd = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dirección
            OutlinedTextField(
                value = address,
                onValueChange = { address = it.capitalize() },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ciudad
            ExposedDropdownMenuBox(
                expanded = cityExpanded,
                onExpandedChange = { cityExpanded = !cityExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = { /* No editable */ },
                    label = { Text("Ciudad") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = cityExpanded,
                    onDismissRequest = { cityExpanded = false }
                ) {
                    cities.forEachIndexed { index, city ->
                        DropdownMenuItem(
                            text = { Text(text = city) },
                            onClick = {
                                if (selectedCity != cities[index]) {
                                    // Resetear la colonia si se cambia la ciudad
                                    selectedNeighborhood = "Ingresa una opción"
                                }
                                selectedCity = cities[index]
                                cityExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Colonias
            ExposedDropdownMenuBox(
                expanded = neighborhoodExpanded,
                onExpandedChange = { neighborhoodExpanded = !neighborhoodExpanded }
            ) {
                OutlinedTextField(
                    value = selectedNeighborhood,
                    onValueChange = { /* No editable */ },
                    label = { Text("Colonia") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = neighborhoodExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = neighborhoodExpanded,
                    onDismissRequest = { neighborhoodExpanded = false }
                ) {
                    neighborhoods.forEachIndexed { index, neighborhood ->
                        DropdownMenuItem(
                            text = { Text(text = neighborhood) },
                            onClick = {
                                selectedNeighborhood = neighborhoods[index]
                                neighborhoodExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Código Postal
            OutlinedTextField(
                value = cp,
                onValueChange = { cp = it },
                label = { Text("Código Postal") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de registro
            Button(
                onClick = {
                    if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || pwd.isEmpty() ||
                        address.isEmpty() || selectedNeighborhood == "Ingresa una opción" || selectedCity == "Ingresa una opción" || cp.isEmpty()
                    ) {
                        Toast.makeText(
                            context,
                            "Por favor llena todos los campos",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (!emailPattern.matcher(email).matches()) {
                        Toast.makeText(context, "Correo inválido", Toast.LENGTH_LONG).show()
                    } else {
                        coroutineScope.launch {
                            val user = UserEntity(
                                name = name,
                                surname = surname,
                                email = email,
                                pwd = pwd,
                                address = address,
                                neighborhood = selectedNeighborhood,
                                city = selectedCity,
                                state = "Nuevo León",
                                cp = cp
                            )
                            db.insertUser(user)
                            Toast.makeText(
                                context,
                                "Usuario registrado localmente",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6200EA))
            ) {
                Text(text = "Registrar", color = Color.White, fontSize = 18.sp)
            }
        }
    }

}

