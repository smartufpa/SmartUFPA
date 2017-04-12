<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Novo usuário</title>
      <!-- Latest compiled and minified CSS bootstrap -->
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
      integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
      crossorigin="anonymous">
        <!-- Angular js e código js -->
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>
      <script src="./js/app.js"></script>
  </head>
  <body ng-app="validationApp" ng-controller="mainController">
      <div class="container">
        <div class="row">
          <h2>Criar usuário</h2>
          <form name="userForm" ng-submit="submitForm(userForm.$valid)" novalidate>

            <!-- USERNAME -->
            <div class="form-group">
              <label for="username">Nome de usuário</label>
              <input type="text" class="form-control" name="username" placeholder="Nome de usuário"
                  required ng-model="user.username" ng-minlength="3" ng-maxlength="12">
              <p ng-show="userForm.name.$invalid && !userForm.name.$pristine" class="help-block">Campo obrigatório</p>
              <p ng-show="userForm.username.$error.minlength" class="help-block">Nome de usuário muito curto.</p>
              <p ng-show="userForm.username.$error.maxlength" class="help-block">Nome de usuário muito longo.</p>
            </div>

            <!-- PASSWORD -->
            <div class="form-group">
              <label for="password">Senha</label>
              <input type="password" class="form-control" name="password" placeholder="Senha"
                  ng-model="user.password" required
                  ng-pattern="regex">
                  <p ng-show="userForm.name.$invalid && !userForm.name.$pristine" class="help-block">Campo obrigatório</p>
                  <p ng-show="userForm.password.$error.minlength" class="help-block">Senha muito curta.</p>
                  <p ng-show="userForm.password.$error.pattern" class="help-block">
                    A senha deve contar ao menos: <b>8 dígitos, letras (uma maiúscula e uma minúscula), números e caracteres especiais</b>
                  </p>
                  <p ng-show="userForm.password.$error.maxlength" class="help-block">Senha muito longa.</p>
            </div>

            <!-- CONFIRM PASSWORD  -->
            <div class="form-group">
              <label for="confirm-password">Confirmar Senha</label>
              <input type="password" class="form-control" name="confirm-password" placeholder="Confirmar Senha"
                  required ng-model="password-confirm" match="user.password" ng-if="userForm.$touched $$ userform"
                  >
            </div>
            <button type="submit" class="btn btn-default" ng-disabled="userForm.$invalid">Entrar</button>
          </form>
        </div>

    </div>

  </body>
</html>
