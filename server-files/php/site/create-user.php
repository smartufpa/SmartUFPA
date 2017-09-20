<?php 
$pageTitle = "Smart UFPA - Criar Usuário";
include "./header.php"?>
  <section ng-app="validationApp" ng-controller="userFormController">
        <div class="row">
          <h2>Criar usuário</h2>
          <form name="userForm" ng-submit="submitUserForm(userForm.$valid)" autocomplete="off" novalidate>
            <!-- TODO mudar ng-show para ng-messages -->
            <!-- USERNAME -->
            <div class="form-group" ng-class="{'has-error':userForm.username.$invalid && userForm.username.$touched }">
              <label for="username">Nome de usuário</label>
              <input type="text" class="form-control" name="username" placeholder="Nome de usuário"
                  required ng-model="user.username" ng-minlength="3" ng-maxlength="12">
              <p ng-show="userForm.username.$invalid && userForm.username.$touched" class="help-block">Campo obrigatório</p>
              <p ng-show="userForm.username.$error.minlength" class="help-block">Nome de usuário muito curto.</p>
              <p ng-show="userForm.username.$error.maxlength" class="help-block">Nome de usuário muito longo.</p>
            </div>

            <!-- PASSWORD -->
            <div class="form-group" ng-class="{'has-error':userForm.password.$invalid && userForm.password.$touched }">
              <label for="password">Senha</label>
              <input type="password" class="form-control" name="password" placeholder="Senha"
                  ng-model="user.password" required
                  ng-pattern="regex">
                  <p ng-show="userForm.password.$invalid && userForm.password.$touched" class="help-block">Campo obrigatório</p>
                  <p ng-show="userForm.password.$error.minlength" class="help-block">Senha muito curta.</p>
                  <p ng-show="userForm.password.$error.pattern" class="help-block">
                    A senha deve contar ao menos: <b>8 dígitos, letras (uma maiúscula e uma minúscula), números e caracteres especiais</b>
                  </p>
                  <p ng-show="userForm.password.$error.maxlength" class="help-block">Senha muito longa.</p>
            </div>

            <!-- CONFIRM PASSWORD  -->
            <div class="form-group" ng-class="{'has-error':(userForm.confirmPassword.$invalid && userForm.confirmPassword.$touched) ||
                              userForm.confirmPassword.$invalid && userForm.confirmPassword.$dirty }">
              <label for="confirm-password">Confirmar Senha</label>
              <input type="password" class="form-control" name="confirmPassword" placeholder="Confirmar Senha"
                  required match-password="password" ng-model="user.confirmPassword"
                  >
                  <div ng-if="userForm.confirmPassword.$error || userForm.confirmPassword.$touched" class="help-block">
                    <p ng-show="(userForm.confirmPassword.$invalid && userForm.confirmPassword.$touched) ||
                                      userForm.confirmPassword.$invalid && userForm.confirmPassword.$dirty" class="help-block">
                      Suas senhas não são iguais.
                    </p>
                  </div>
            </div>
            <button type="submit" class="btn btn-default" ng-disabled="userForm.$invalid">Entrar</button>
          </form>
        </div>




    

  </section>
<script src="js/validation-app.js"></script>
<?php include "./footer.php"?>
