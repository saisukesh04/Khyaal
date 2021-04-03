import discord
from discord.ext import commands 
import random 
import requests
from datetime import date 


import keys


client = commands.Bot(command_prefix= '.')



@client.event
async def on_ready():
    print("Khyaal Bot is ready")


@client.command()
async def ping(ctx):
    await ctx.send(f'Pong! Latency = {client.latency * 1000} ms')

@client.command()
async def hi(ctx):
    await ctx.send(f'Hi!')

@client.command()
async def test(ctx, arg1, arg2):
    url1 = 'https://api.edamam.com/api/food-database/v2/parser?ingr={}&app_id={}&app_key={}'.format(arg1,keys.ID,keys.APP_KEY)
    url2 = 'https://api.edamam.com/api/food-database/v2/parser?ingr={}&app_id={}&app_key={}'.format(arg2,keys.ID,keys.APP_KEY)
    print(requests.get(url1).status_code)
    print(requests.get(url2).text)
    x = requests.get(url2).json()
    z = requests.get(url1).json()
    y = x['parsed'][0]['food']['nutrients']
    k = z['parsed'][0]['food']['nutrients']
    await ctx.send(f'{y},\n {k}')


client.run(keys.botToken)
