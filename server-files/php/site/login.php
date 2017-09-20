<?php include "./header.php";
$pageTitle = "Smart UFPA - Login";
include_once ROOT . 'session.php';
?>
<section>
	<?php if(!$loggedIn){?>
		<h2>Entrar</h2>
	<form action="../validate-login.php" method="post" name="loginForm">
		<!--Username -->
		<div class="form-group">
			<label for="username">Nome de usuário: </label> <input
				class="form-control" type="text" name="username" required
				placeholder="Nome de usuário">
		</div>
		<!--Password -->
		<div class="form-group">
			<label for="password">Senha: </label> <input class="form-control"
				type="password" name="password" required placeholder="Senha">
		</div>
			<?php if(isset($_GET['error'])){
					if($_GET['error'] == 1){		?>
			<div>
			<p style="color: #ff3025;">Usuário ou senha estão incorretos.</p>
		</div>
			<?php }else if($_GET['error'] == 2){ ?>
			<div>
				<p style="color: #ff3025;">O usuário não possui a permissão para acessar esta área.</p>
			</div>
			
			<?php }
			
			}?>
			
			
			<button type="submit" class="btn btn-default">Entrar</button>

	</form>
	<?php }else{ header("Location: moderate.php"); } ?>
</section>
<?php include "./footer.php"?>