<?php 
include_once dirname(__DIR__) . '/session.php';
$loggedIn = SessionClient::checkIfLoggedIn();
?> 

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Login - Smart UFPA</title>
<link rel="stylesheet"
	href="css/bootstrap-3.3.7-dist/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"
	integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
	crossorigin="anonymous"></script>
</head>
<body>
	<div class="container">
	<?php if(!$loggedIn){?>
		<h2>Entrar</h2>
		<form action="../validate-login.php" method="post" name="loginForm">
			<!--Username -->
			<div class="form-group">
				<label for="username">Nome de usuário: </label>
				<input
					class="form-control" type="text" name="username" required
					placeholder="Nome de usuário">
			</div>
			<!--Password -->
			<div class="form-group">
				<label for="password">Senha: </label> <input class="form-control"
					type="password" name="password" required placeholder="Senha">
			</div>
			<?php if(isset($_GET['error'])){ ?>
			<div>
				<p style="color: #ff3025;">
				  Usuário ou senha estão incorretos.
				</p>
			</div>
			<?php } ?>
			
			
			<button type="submit" class="btn btn-default">Entrar</button>

		</form>
	<?php }else{ header("Location: moderate.php"); } ?>
	</div>
</body>
</html>