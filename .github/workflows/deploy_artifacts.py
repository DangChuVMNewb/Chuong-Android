import os
import asyncio
from telethon import TelegramClient
from telethon.errors import SessionPasswordNeededError
import logging

# Setup logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

async def main():
    api_id = int(os.getenv('API_ID'))
    api_hash = os.getenv('API_HASH')
    bot_token = os.getenv('BOT_TOKEN')
    chat_id = int(os.getenv('CHAT_ID'))
    topic_id = os.getenv('TOPIC_ID')
    apk_path = os.getenv('APK_PATH')
    
    # Extract commit info from environment or derive from file name
    commit_info = os.getenv('COMMIT_MESSAGE', 'Latest Build')
    author = os.getenv('COMMIT_AUTHOR', 'Unknown')
    
    # Initialize client
    client = TelegramClient('bot_session', api_id, api_hash, bot_token=bot_token)
    
    try:
        await client.start()
        
        # Check if we can connect as bot
        me = await client.get_me()
        if not me.bot:
            logging.error("Client is not running as bot")
            return
        
        # Prepare caption
        caption = f"📦 New APK Build Available!\n\nAuthor: {author}\nCommit: {commit_info}\n\n#APK #Build"
        
        # Send the APK file
        upload_kwargs = {}
        if topic_id:
            upload_kwargs['reply_to'] = int(topic_id)
            
        await client.send_file(
            chat_id,
            apk_path,
            caption=caption,
            force_document=True,
            **upload_kwargs
        )
        
        logging.info("APK sent successfully!")
        
    except Exception as e:
        logging.error(f"Error sending APK: {str(e)}")
    finally:
        await client.disconnect()

if __name__ == "__main__":
    asyncio.run(main())