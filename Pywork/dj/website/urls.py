from django.contrib import admin
from django.urls import path
import views

urlpatterns = [
    path('', views.show),
    path('admin/', admin.site.urls),
]