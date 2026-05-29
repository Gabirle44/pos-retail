# 🚀 Cómo subir el proyecto a GitHub

Sigue estos pasos para tener el código público en GitHub y poder incluir el enlace en el documento entregable.

---

## 1. Crear cuenta en GitHub

Si no tienes cuenta, ve a https://github.com/signup y crea una.

---

## 2. Crear el repositorio

1. En GitHub, clic en el botón **`+`** arriba a la derecha → **New repository**.
2. **Repository name:** `pos-retail`
3. **Description:** `Sistema POS - Proyecto Análisis de Sistemas I`
4. **Visibilidad:** ✅ **Public** (importante: si lo dejas privado, el catedrático no podrá verlo)
5. **NO** marques "Add a README file" (ya tenemos uno).
6. **NO** marques `.gitignore` ni licencia.
7. Clic en **Create repository**.

---

## 3. Subir el código desde tu computadora

Abre una terminal (PowerShell, Git Bash o terminal de tu IDE) dentro de la carpeta `pos-retail/` y ejecuta:

```bash
# 1. Inicializar repositorio Git
git init

# 2. Configurar tu identidad (solo la primera vez)
git config user.email "tu_correo@ejemplo.com"
git config user.name "Tu Nombre"

# 3. Agregar todos los archivos
git add .

# 4. Hacer el primer commit
git commit -m "POS Retail - proyecto completo"

# 5. Configurar la rama principal
git branch -M main

# 6. Conectar con GitHub (reemplaza TU_USUARIO)
git remote add origin https://github.com/TU_USUARIO/pos-retail.git

# 7. Subir el código
git push -u origin main
```

> Si te pide credenciales: el usuario es tu username de GitHub, y el password ya no es tu contraseña normal. Tienes que crear un **Personal Access Token**:
> 1. Ve a GitHub → tu foto → Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate new token.
> 2. Marca el permiso `repo`.
> 3. Copia el token y úsalo como contraseña en `git push`.

---

## 4. Verificar que esté público

1. Abre tu repositorio en una **ventana de incógnito** del navegador.
2. URL: `https://github.com/TU_USUARIO/pos-retail`
3. Si lo ves sin loguearte, está bien.
4. Si te pide login, ve a *Settings → Danger Zone → Change repository visibility → Public*.

---

## 5. Hacer commits incrementales (recomendado para mostrar avance)

En vez de subir todo de una vez, puedes hacer commits por sprint para que se vea el avance en GitHub:

```bash
# Sprint 1: documentación
git add docs/ sql/
git commit -m "Sprint 1: DERCAS y modelo ER"
git push

# Sprint 2: módulo login
git add src/main/java/com/posretail/security/ src/main/java/com/posretail/config/
git commit -m "Sprint 2: módulo de seguridad con BCrypt y bloqueo"
git push

# Sprint 3: compras
git add src/main/java/com/posretail/service/CompraService.java src/main/java/com/posretail/controller/CompraController.java
git commit -m "Sprint 3: módulo de compras con devoluciones"
git push

# Sprint 4: ventas
git add src/main/java/com/posretail/service/VentaService.java
git commit -m "Sprint 4: módulo de ventas con comprobantes"
git push
```

Esto se ve mejor en el historial de commits y demuestra trabajo iterativo.

---

## 6. Copiar el enlace para el PDF

El enlace que pondrás en el documento entregable es exactamente:

```
https://github.com/TU_USUARIO/pos-retail
```

---

## ❓ Problemas comunes

### "fatal: not a git repository"
Estás en la carpeta equivocada. Asegúrate de estar dentro de `pos-retail/`.

### "Authentication failed"
GitHub ya no acepta password normal. Usa un Personal Access Token (instrucciones arriba).

### "Updates were rejected"
Alguien (¿tú mismo desde otro lado?) ya hizo push. Ejecuta:
```bash
git pull --rebase
git push
```

### "Repository not found"
Verifica que copiaste bien la URL y que el repositorio existe en tu cuenta.
