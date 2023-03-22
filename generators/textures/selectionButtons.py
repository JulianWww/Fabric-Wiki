from wget import download
from os import mkdir
import numpy as np
import cv2

tmpDir = ".tmp"
try: mkdir(tmpDir)
except FileExistsError: pass

filePath = download("https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.19.3/assets/minecraft/textures/gui/resource_packs.png", out=tmpDir)
guiPath = "./src/main/resources/assets/wiki/textures/gui/"

img = cv2.imread(filePath, cv2.IMREAD_UNCHANGED)

def generateButtonGroup(img, x):
  out = np.zeros((32, 64, img.shape[2]), dtype=np.int16)

  orig = img[32*(x): 32*(x+1), :32, :]

  out[:32, :32] = orig
  out[:, 32:, :] = np.swapaxes(orig,0,1)
  return out


buttons = 2
out = []
for idx in range(buttons):
  out.append(generateButtonGroup(img, idx))

cv2.imwrite(guiPath + "pageselection.png", np.concatenate(out, 0))