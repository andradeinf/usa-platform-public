angular.module("franchisorLogistics")
    .config(function($provide) {
        // this demonstrates how to register a new tool and add it to the default toolbar
        $provide.decorator('taOptions', ['taRegisterTool', '$delegate', function(taRegisterTool, taOptions) { // $delegate is the taOptions we are decorating
        	
        	taRegisterTool('uploadImage', {
        		display: '<button type="button" name="uploadImage" title="Adicionar imagem do computador"><i class="fa fa-upload"></i></button>',
                action: function (deferred,restoreSelection) {
                	this.$editor().$parent.openUploadImage(deferred,restoreSelection);
                    return false;
                }
            });        	
        	
            return taOptions;
        }]);
        
        $provide.decorator('taTranslations', function($delegate) {
            
        	$delegate.heading.tooltip = 'Título ';
        	$delegate.p.tooltip = 'Parágrafo';
        	$delegate.pre.tooltip = 'Texto pré-formatado';
        	$delegate.quote.tooltip = 'Citação';
        	
        	$delegate.bold.tooltip = 'Negrito';
            $delegate.italic.tooltip = 'Itálico';
            $delegate.underline.tooltip = 'Sublinhado';
            $delegate.strikeThrough.tooltip = 'Tachado';
            $delegate.ul.tooltip = 'Lista com marcadores';
        	$delegate.ol.tooltip = 'Lista ordenada';        	
        	$delegate.redo.tooltip = 'Refazer';
        	$delegate.undo.tooltip = 'Desfazer';
            $delegate.clear.tooltip = 'Limpar formatação';
            
            $delegate.justifyLeft.tooltip = 'Alinhar à esquerda';
            $delegate.justifyCenter.tooltip = 'Centralizar';
            $delegate.justifyRight.tooltip = 'Alinhar à direita';            
            $delegate.indent.tooltip = 'Adicionar indentação';
            $delegate.outdent.tooltip = 'Remover indentação';            
            
            $delegate.html.tooltip = 'Ver HTML';
            $delegate.insertImage.tooltip = 'Adicionar imagem da internet';
            $delegate.insertImage.dialogPrompt = 'URL da imagem na internet';
            $delegate.insertLink.tooltip = 'Adicionar link';
            $delegate.insertLink.dialogPrompt = "URL do link";
            $delegate.editLink.targetToggle.buttontext = "Abrir em outra janela";
            $delegate.editLink.reLinkButton.tooltip = "Editar link";
            $delegate.editLink.unLinkButton.tooltip = "Remover link";
            $delegate.insertVideo.tooltip = 'Adicionar video do Youtube';
            $delegate.insertVideo.dialogPrompt = 'URL do video no Youtube';
            
            return $delegate;
          });
    })
