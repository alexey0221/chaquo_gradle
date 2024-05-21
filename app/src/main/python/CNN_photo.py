from PIL import Image
import torch
# import torchvision.transforms as transforms
import numpy as np
import os
from os.path import dirname, join
from com.chaquo.python import Python



def photoFindText(image_path):
    # CNN_path = "python/source_NN_models/model_cnn_photo_classification_GPU_reshape_without_print.pt"
    # context = Python.getPlatform().getApplication()
    # context.

    CNN_path = join(dirname(__file__), "source_NN_models/model_cnn_photo_classification_GPU_reshape_without_print.pt")

    # Открываем изображение с помощью библиотеки Pillow
    img = Image.open(image_path)

    resized_img = img.resize((200, 200))
    resized_img = np.array(resized_img)

    desired_shape = np.array([200, 200, 3])
    if not np.array_equal(resized_img.shape, desired_shape):
        # print("Error")
        return "Error" #torch.tensor([-1.0], requires_grad=True)
    # print(f'resized_img.shape = {resized_img.shape}')

    tensorImg = torch.empty(1, 200, 200, 3)

    tensorImg[0] = torch.tensor(resized_img)

    tensorImg = tensorImg / 255

    tensorImg = tensorImg.permute(0, 3, 1, 2)

    # cnn = CNNet()

    # cnn.load_state_dict(torch.load('source_NN_models/cnn_model_weight.pth',map_location=torch.device('cpu')))
    # cnn.eval()
    # preds = cnn.forward(tensorImg)
    CNN_path_tuple = "/".join(CNN_path.split("/"))#tuple(CNN_path.split("/"))
    path = os.path.normpath(CNN_path)
    # return  path
    model_photo = torch.jit.load("/data/data/com.example.chaquo_gradle/files/chaquopy/AssetFinder/app/source_NN_models/model_cnn_photo_classification_GPU_reshape_without_print.pt",map_location=torch.device('cpu'))
    return str(model_photo.code)
    model_photo.eval()
    # print(tensorImg.shape)
    confidence = model_photo(tensorImg)
    # print(f'confidence.shape: {confidence.shape}')
    # confidence = cnn(tensorImg)
    # print(f'Предсказание модели confidence: {confidence}')
    # print(f'Предсказание модели preds: {preds}')
    return confidence[0].item()