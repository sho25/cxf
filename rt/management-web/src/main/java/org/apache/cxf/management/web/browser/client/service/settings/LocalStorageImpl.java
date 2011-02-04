begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|web
operator|.
name|browser
operator|.
name|client
operator|.
name|service
operator|.
name|settings
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nonnull
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JsArray
import|;
end_import

begin_class
specifier|public
class|class
name|LocalStorageImpl
implements|implements
name|LocalStorage
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SETTINGS_KEY
init|=
literal|"logBrowser.settings"
decl_stmt|;
specifier|private
name|Boolean
name|isAvailable
decl_stmt|;
specifier|public
name|boolean
name|isAvailable
parameter_list|()
block|{
if|if
condition|(
name|isAvailable
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|isAvailable
operator|=
name|checkIsAvailable
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|isAvailable
return|;
block|}
specifier|private
specifier|native
name|boolean
name|checkIsAvailable
parameter_list|()
comment|/*-{         var key = "isLocalStorageAvailable";         $wnd.$.jStorage.set(key, true);         return !($wnd.$.jStorage.get(key) == null);     }-*/
function_decl|;
specifier|public
name|void
name|saveSettings
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|Settings
name|settings
parameter_list|)
block|{
if|if
condition|(
name|isAvailable
argument_list|()
condition|)
block|{
name|put
argument_list|(
name|SETTINGS_KEY
argument_list|,
name|Converter
operator|.
name|convertToLocalSettings
argument_list|(
name|settings
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Nullable
specifier|public
name|Settings
name|retrieveSettings
parameter_list|()
block|{
if|if
condition|(
name|isAvailable
argument_list|()
condition|)
block|{
name|LocalSettings
name|localSettings
init|=
operator|(
name|LocalSettings
operator|)
name|get
argument_list|(
name|SETTINGS_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|localSettings
operator|!=
literal|null
condition|)
block|{
return|return
name|Converter
operator|.
name|convertToSettings
argument_list|(
name|localSettings
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
if|if
condition|(
name|isAvailable
argument_list|()
condition|)
block|{
name|remove
argument_list|(
name|SETTINGS_KEY
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|native
name|void
name|put
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|String
name|key
parameter_list|,
annotation|@
name|Nonnull
specifier|final
name|JavaScriptObject
name|obj
parameter_list|)
comment|/*-{         $wnd.$.jStorage.set(key, obj);     }-*/
function_decl|;
annotation|@
name|Nullable
specifier|private
specifier|native
name|JavaScriptObject
name|get
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|String
name|key
parameter_list|)
comment|/*-{         return $wnd.$.jStorage.get(key);     }-*/
function_decl|;
specifier|private
specifier|native
name|void
name|remove
parameter_list|(
annotation|@
name|Nonnull
specifier|final
name|String
name|key
parameter_list|)
comment|/*-{         $wnd.$.jStorage.deleteKey(key);     }-*/
function_decl|;
specifier|private
specifier|static
specifier|final
class|class
name|Converter
block|{
specifier|private
name|Converter
parameter_list|()
block|{ }
annotation|@
name|Nullable
specifier|public
specifier|static
name|Settings
name|convertToSettings
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|LocalSettings
name|src
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Settings
name|dst
init|=
operator|new
name|Settings
argument_list|()
decl_stmt|;
name|dst
operator|.
name|setCredentials
argument_list|(
name|convertToCredentials
argument_list|(
name|src
operator|.
name|getCredentials
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|JsArray
argument_list|<
name|LocalSubscription
argument_list|>
name|subscriptions
init|=
name|src
operator|.
name|getSubscriptions
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|subscriptions
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Subscription
name|subscription
init|=
name|convertToSubscription
argument_list|(
name|subscriptions
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|subscription
operator|!=
literal|null
condition|)
block|{
name|dst
operator|.
name|getSubscriptions
argument_list|()
operator|.
name|add
argument_list|(
name|subscription
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dst
return|;
block|}
annotation|@
name|Nullable
specifier|private
specifier|static
name|Credentials
name|convertToCredentials
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|LocalCredentials
name|src
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|Credentials
argument_list|(
name|src
operator|.
name|getUsername
argument_list|()
argument_list|,
name|src
operator|.
name|getPassword
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Nullable
specifier|private
specifier|static
name|Subscription
name|convertToSubscription
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|LocalSubscription
name|src
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|Subscription
argument_list|(
name|src
operator|.
name|getId
argument_list|()
argument_list|,
name|src
operator|.
name|getName
argument_list|()
argument_list|,
name|src
operator|.
name|getURL
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Nullable
specifier|public
specifier|static
name|LocalSettings
name|convertToLocalSettings
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|Settings
name|src
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|LocalSettings
name|dst
init|=
operator|(
name|LocalSettings
operator|)
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
decl_stmt|;
name|dst
operator|.
name|setCredentials
argument_list|(
name|convertToLocalCredentials
argument_list|(
name|src
operator|.
name|getCredentials
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|JsArray
argument_list|<
name|LocalSubscription
argument_list|>
name|dstSubscriptions
init|=
operator|(
name|JsArray
argument_list|<
name|LocalSubscription
argument_list|>
operator|)
name|JavaScriptObject
operator|.
name|createArray
argument_list|()
decl_stmt|;
for|for
control|(
name|Subscription
name|subscription
range|:
name|src
operator|.
name|getSubscriptions
argument_list|()
control|)
block|{
name|dstSubscriptions
operator|.
name|push
argument_list|(
name|convertToLocalSubscription
argument_list|(
name|subscription
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|dst
operator|.
name|setSubscriptions
argument_list|(
name|dstSubscriptions
argument_list|)
expr_stmt|;
return|return
name|dst
return|;
block|}
annotation|@
name|Nullable
specifier|private
specifier|static
name|LocalCredentials
name|convertToLocalCredentials
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|Credentials
name|src
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|LocalCredentials
name|dst
init|=
operator|(
name|LocalCredentials
operator|)
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
decl_stmt|;
name|dst
operator|.
name|setUsername
argument_list|(
name|src
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setPassword
argument_list|(
name|src
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dst
return|;
block|}
annotation|@
name|Nullable
specifier|private
specifier|static
name|LocalSubscription
name|convertToLocalSubscription
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|Subscription
name|src
parameter_list|)
block|{
if|if
condition|(
name|src
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|LocalSubscription
name|dst
init|=
operator|(
name|LocalSubscription
operator|)
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
decl_stmt|;
name|dst
operator|.
name|setId
argument_list|(
name|src
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setName
argument_list|(
name|src
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dst
operator|.
name|setURL
argument_list|(
name|src
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dst
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|LocalSettings
extends|extends
name|JavaScriptObject
block|{
specifier|protected
name|LocalSettings
parameter_list|()
block|{ }
specifier|public
specifier|final
specifier|native
name|void
name|setCredentials
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|LocalCredentials
name|credentials
parameter_list|)
comment|/*-{             this.credentials = credentials;         }-*/
function_decl|;
annotation|@
name|Nullable
specifier|public
specifier|final
specifier|native
name|LocalCredentials
name|getCredentials
parameter_list|()
comment|/*-{             return this.credentials;         }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|void
name|setSubscriptions
parameter_list|(
annotation|@
name|Nullable
name|JsArray
argument_list|<
name|LocalSubscription
argument_list|>
name|subscriptions
parameter_list|)
comment|/*-{             this.subscriptions = subscriptions;         }-*/
function_decl|;
annotation|@
name|Nonnull
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|LocalSubscription
argument_list|>
name|getSubscriptions
parameter_list|()
comment|/*-{             if (this.subscriptions != null) {                 return this.subscriptions;             } else {                 return [];             }         }-*/
function_decl|;
block|}
specifier|private
specifier|static
class|class
name|LocalCredentials
extends|extends
name|JavaScriptObject
block|{
specifier|protected
name|LocalCredentials
parameter_list|()
block|{ }
specifier|public
specifier|final
specifier|native
name|void
name|setUsername
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|String
name|username
parameter_list|)
comment|/*-{             this.username = username;         }-*/
function_decl|;
annotation|@
name|Nullable
specifier|public
specifier|final
specifier|native
name|String
name|getUsername
parameter_list|()
comment|/*-{             return this.username;         }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|void
name|setPassword
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|String
name|password
parameter_list|)
comment|/*-{             this.password = password;         }-*/
function_decl|;
annotation|@
name|Nullable
specifier|public
specifier|final
specifier|native
name|String
name|getPassword
parameter_list|()
comment|/*-{              return this.password;         }-*/
function_decl|;
block|}
specifier|private
specifier|static
class|class
name|LocalSubscription
extends|extends
name|JavaScriptObject
block|{
specifier|protected
name|LocalSubscription
parameter_list|()
block|{ }
specifier|public
specifier|final
specifier|native
name|void
name|setId
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|String
name|id
parameter_list|)
comment|/*-{             this.id = id;         }-*/
function_decl|;
annotation|@
name|Nullable
specifier|public
specifier|final
specifier|native
name|String
name|getId
parameter_list|()
comment|/*-{             return this.id;         }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|void
name|setName
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|String
name|name
parameter_list|)
comment|/*-{             this.name = name;         }-*/
function_decl|;
annotation|@
name|Nullable
specifier|public
specifier|final
specifier|native
name|String
name|getName
parameter_list|()
comment|/*-{             return this.name;         }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|void
name|setURL
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|String
name|url
parameter_list|)
comment|/*-{             this.url = url;         }-*/
function_decl|;
annotation|@
name|Nullable
specifier|public
specifier|final
specifier|native
name|String
name|getURL
parameter_list|()
comment|/*-{             return this.url;         }-*/
function_decl|;
block|}
block|}
end_class

end_unit

