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
name|js
operator|.
name|rhino
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|ContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Script
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Scriptable
import|;
end_import

begin_class
specifier|public
class|class
name|ProviderFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ILLEGAL_SVCMD_MODE
init|=
literal|": unknown ServiceMode: "
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ILLEGAL_SVCMD_TYPE
init|=
literal|": ServiceMode value must be of type string"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NO_SUCH_FILE
init|=
literal|": file does not exist"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NO_PROVIDER
init|=
literal|": file contains no WebServiceProviders"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ProviderFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|epAddress
decl_stmt|;
specifier|private
name|boolean
name|isBaseAddr
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AbstractDOMProvider
argument_list|>
name|providers
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|AbstractDOMProvider
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|ContextFactory
operator|.
name|initGlobal
argument_list|(
operator|new
name|RhinoContextFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ProviderFactory
parameter_list|(
name|String
name|baseAddr
parameter_list|)
block|{
name|epAddress
operator|=
name|baseAddr
expr_stmt|;
name|isBaseAddr
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|ProviderFactory
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
name|void
name|createAndPublish
parameter_list|(
name|File
name|f
parameter_list|,
name|String
name|epAddr
parameter_list|,
name|boolean
name|isBase
parameter_list|)
throws|throws
name|Exception
block|{
name|publishImpl
argument_list|(
name|f
argument_list|,
name|epAddr
argument_list|,
name|isBase
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|createAndPublish
parameter_list|(
name|File
name|f
parameter_list|)
throws|throws
name|Exception
block|{
name|publishImpl
argument_list|(
name|f
argument_list|,
name|epAddress
argument_list|,
name|isBaseAddr
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|publishImpl
parameter_list|(
name|File
name|f
parameter_list|,
name|String
name|epAddr
parameter_list|,
name|boolean
name|isBase
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|f
operator|.
name|getPath
argument_list|()
operator|+
name|NO_SUCH_FILE
argument_list|)
throw|;
block|}
name|boolean
name|isE4X
init|=
name|f
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".jsx"
argument_list|)
decl_stmt|;
name|BufferedReader
name|bufrd
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|f
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
literal|null
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|line
operator|=
name|bufrd
operator|.
name|readLine
argument_list|()
expr_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|sb
operator|.
name|append
argument_list|(
name|line
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|String
name|scriptStr
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Context
name|cx
init|=
name|ContextFactory
operator|.
name|getGlobal
argument_list|()
operator|.
name|enterContext
argument_list|()
decl_stmt|;
name|boolean
name|providerFound
init|=
literal|false
decl_stmt|;
try|try
block|{
name|Scriptable
name|scriptScope
init|=
name|cx
operator|.
name|initStandardObjects
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Object
index|[]
name|ids
init|=
name|compileScript
argument_list|(
name|cx
argument_list|,
name|scriptStr
argument_list|,
name|scriptScope
argument_list|,
name|f
argument_list|)
decl_stmt|;
if|if
condition|(
name|ids
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|Service
operator|.
name|Mode
name|mode
init|=
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
decl_stmt|;
for|for
control|(
name|Object
name|idObj
range|:
name|ids
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|idObj
operator|instanceof
name|String
operator|)
condition|)
block|{
continue|continue;
block|}
name|String
name|id
init|=
operator|(
name|String
operator|)
name|idObj
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|startsWith
argument_list|(
literal|"WebServiceProvider"
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Object
name|obj
init|=
name|scriptScope
operator|.
name|get
argument_list|(
name|id
argument_list|,
name|scriptScope
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Scriptable
operator|)
condition|)
block|{
continue|continue;
block|}
name|Scriptable
name|wspVar
init|=
operator|(
name|Scriptable
operator|)
name|obj
decl_stmt|;
name|providerFound
operator|=
literal|true
expr_stmt|;
name|obj
operator|=
name|wspVar
operator|.
name|get
argument_list|(
literal|"ServiceMode"
argument_list|,
name|wspVar
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|!=
name|Scriptable
operator|.
name|NOT_FOUND
condition|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|String
condition|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
if|if
condition|(
literal|"PAYLOAD"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|mode
operator|=
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"MESSAGE"
operator|.
name|equalsIgnoreCase
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|mode
operator|=
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|f
operator|.
name|getPath
argument_list|()
operator|+
name|ILLEGAL_SVCMD_MODE
operator|+
name|value
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|f
operator|.
name|getPath
argument_list|()
operator|+
name|ILLEGAL_SVCMD_TYPE
argument_list|)
throw|;
block|}
block|}
name|AbstractDOMProvider
name|provider
init|=
name|createProvider
argument_list|(
name|mode
argument_list|,
name|scriptScope
argument_list|,
name|wspVar
argument_list|,
name|epAddr
argument_list|,
name|isBase
argument_list|,
name|isE4X
argument_list|)
decl_stmt|;
try|try
block|{
name|provider
operator|.
name|publish
argument_list|()
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AbstractDOMProvider
operator|.
name|JSDOMProviderException
name|ex
parameter_list|)
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|(
name|f
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
operator|.
name|append
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|Exception
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
finally|finally
block|{
name|Context
operator|.
name|exit
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|providerFound
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|f
operator|.
name|getPath
argument_list|()
operator|+
name|NO_PROVIDER
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
while|while
condition|(
operator|!
name|providers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|AbstractDOMProvider
name|p
init|=
name|providers
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|p
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|AbstractDOMProvider
name|createProvider
parameter_list|(
name|Service
operator|.
name|Mode
name|mode
parameter_list|,
name|Scriptable
name|scope
parameter_list|,
name|Scriptable
name|wsp
parameter_list|,
name|String
name|epAddr
parameter_list|,
name|boolean
name|isBase
parameter_list|,
name|boolean
name|e4x
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|String
name|modestr
init|=
operator|(
name|mode
operator|==
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
operator|)
condition|?
literal|"payload"
else|:
literal|"message"
decl_stmt|;
name|String
name|type
init|=
name|e4x
condition|?
literal|"E4X"
else|:
literal|"JavaScript"
decl_stmt|;
name|String
name|base
init|=
name|isBase
condition|?
literal|"base "
else|:
literal|""
decl_stmt|;
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"creating a "
argument_list|)
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|modestr
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|" provider for "
argument_list|)
operator|.
name|append
argument_list|(
name|base
argument_list|)
operator|.
name|append
argument_list|(
literal|"address "
argument_list|)
operator|.
name|append
argument_list|(
name|epAddr
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|AbstractDOMProvider
name|provider
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mode
operator|==
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
condition|)
block|{
name|provider
operator|=
operator|new
name|DOMPayloadProvider
argument_list|(
name|scope
argument_list|,
name|wsp
argument_list|,
name|epAddr
argument_list|,
name|isBase
argument_list|,
name|e4x
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mode
operator|==
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
condition|)
block|{
name|provider
operator|=
operator|new
name|DOMMessageProvider
argument_list|(
name|scope
argument_list|,
name|wsp
argument_list|,
name|epAddr
argument_list|,
name|isBase
argument_list|,
name|e4x
argument_list|)
expr_stmt|;
block|}
return|return
name|provider
return|;
block|}
specifier|private
name|Object
index|[]
name|compileScript
parameter_list|(
name|Context
name|cx
parameter_list|,
name|String
name|scriptStr
parameter_list|,
name|Scriptable
name|scriptScope
parameter_list|,
name|File
name|f
parameter_list|)
block|{
name|int
name|opt
init|=
name|cx
operator|.
name|getOptimizationLevel
argument_list|()
decl_stmt|;
name|cx
operator|.
name|setOptimizationLevel
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Script
name|script
init|=
name|cx
operator|.
name|compileString
argument_list|(
name|scriptStr
argument_list|,
name|f
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|script
operator|.
name|exec
argument_list|(
name|cx
argument_list|,
name|scriptScope
argument_list|)
expr_stmt|;
name|Object
index|[]
name|ids
init|=
name|scriptScope
operator|.
name|getIds
argument_list|()
decl_stmt|;
name|cx
operator|.
name|setOptimizationLevel
argument_list|(
name|opt
argument_list|)
expr_stmt|;
name|script
operator|=
name|cx
operator|.
name|compileString
argument_list|(
name|scriptStr
argument_list|,
name|f
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|script
operator|.
name|exec
argument_list|(
name|cx
argument_list|,
name|scriptScope
argument_list|)
expr_stmt|;
return|return
name|ids
return|;
block|}
specifier|static
class|class
name|RhinoContextFactory
extends|extends
name|ContextFactory
block|{
specifier|public
name|boolean
name|hasFeature
parameter_list|(
name|Context
name|cx
parameter_list|,
name|int
name|feature
parameter_list|)
block|{
if|if
condition|(
name|feature
operator|==
name|Context
operator|.
name|FEATURE_DYNAMIC_SCOPE
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|super
operator|.
name|hasFeature
argument_list|(
name|cx
argument_list|,
name|feature
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

