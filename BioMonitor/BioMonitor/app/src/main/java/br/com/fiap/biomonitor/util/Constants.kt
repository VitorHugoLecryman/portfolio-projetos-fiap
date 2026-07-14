package br.com.fiap.biomonitor.util

object Constants {


    const val INATURALIST_BASE_URL = "https://api.inaturalist.org/v1/"
    const val GBIF_BASE_URL = "https://api.gbif.org/v1/"


    const val TAXA_AUTOCOMPLETE_ENDPOINT = "taxa/autocomplete"
    const val TAXA_DETAIL_ENDPOINT = "taxa/{id}"
    const val SPECIES_ENDPOINT = "species/{key}"
    const val SPECIES_SEARCH_ENDPOINT = "species/search"


    const val NETWORK_TIMEOUT_SECONDS = 30L
    const val AUTOCOMPLETE_RESULTS_LIMIT = 10


    const val SESSION_TIMEOUT_MS = 7 * 24 * 60 * 60 * 1000L


    const val PREFS_NAME = "biomonitor_session"
    const val KEY_USER_ID = "user_id"
    const val KEY_EMAIL = "email"
    const val KEY_LOGIN_TIME = "login_time"


    const val DATABASE_NAME = "biomonitor.db"


    const val MIN_PASSWORD_LENGTH = 6
    const val MIN_NAME_LENGTH = 3


    const val RANKING_TOP_LIMIT = 10


    const val IMAGE_COMPRESSION_QUALITY = 85
    const val MAX_IMAGE_DIMENSION = 1920
}

object ErrorMessages {

    const val EMAIL_REQUIRED = "Email é obrigatório"
    const val INVALID_EMAIL = "Email inválido"
    const val INVALID_PASSWORD = "Senha deve ter pelo menos 6 caracteres"
    const val INVALID_PASSWORD_STRENGTH = "Senha deve conter letras e números"
    const val PASSWORDS_DONT_MATCH = "Senhas não coincidem"
    const val INVALID_CREDENTIALS = "Email ou senha incorretos"
    const val EMAIL_ALREADY_EXISTS = "Este email já está cadastrado"


    const val INVALID_NAME = "Nome deve ter pelo menos 3 caracteres"
    const val INVALID_CITY = "Cidade é obrigatória"
    const val TERMS_NOT_ACCEPTED = "Você deve aceitar os termos de uso"


    const val PHOTO_REQUIRED = "Selecione uma foto"
    const val CATEGORY_REQUIRED = "Selecione uma categoria"
    const val SPECIES_REQUIRED = "Informe o nome da espécie"
    const val LOCATION_REQUIRED = "Localização é obrigatória"


    const val NO_INTERNET = "Sem conexão com a internet"
    const val SERVER_ERROR = "Erro no servidor. Tente novamente."
    const val EMPTY_RESPONSE = "Resposta vazia do servidor"
    const val UNKNOWN_ERROR = "Erro inesperado. Tente novamente."
    const val TIMEOUT_ERROR = "Tempo de conexão esgotado"


    const val DATABASE_ERROR = "Erro no banco de dados"
    const val DUPLICATE_DATA = "Dados duplicados ou inválidos"


    const val DELETE_CONFIRMATION = "Tem certeza que deseja excluir?"
    const val LOGOUT_CONFIRMATION = "Tem certeza que deseja sair?"
    const val ACCOUNT_DELETE_CONFIRMATION = "Esta ação é irreversível. Todos os seus dados serão excluídos."


    const val REGISTRATION_SUCCESS = "Cadastro realizado com sucesso!"
    const val SIGHTING_SAVED = "Avistamento salvo com sucesso!"
    const val PROFILE_UPDATED = "Perfil atualizado com sucesso!"
    const val SIGHTING_DELETED = "Avistamento excluído com sucesso!"


    const val LOCATION_PERMISSION_DENIED = "Permissão de localização negada"
    const val LOCATION_UNAVAILABLE = "Localização indisponível"


    const val CAMERA_PERMISSION_DENIED = "Permissão de câmera negada"
    const val CAMERA_ERROR = "Erro ao acessar a câmera"


    const val IMAGE_SAVE_ERROR = "Erro ao salvar imagem"
    const val IMAGE_LOAD_ERROR = "Erro ao carregar imagem"
}
