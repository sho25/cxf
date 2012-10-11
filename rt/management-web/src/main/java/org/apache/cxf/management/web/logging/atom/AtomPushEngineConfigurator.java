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
name|logging
operator|.
name|atom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|Converter
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|StandardConverter
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|StandardConverter
operator|.
name|Format
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|StandardConverter
operator|.
name|Multiplicity
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|converter
operator|.
name|StandardConverter
operator|.
name|Output
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|deliverer
operator|.
name|Deliverer
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|deliverer
operator|.
name|RetryingDeliverer
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|deliverer
operator|.
name|WebClientDeliverer
import|;
end_import

begin_comment
comment|/**  * Package private interpreter of incomplete input of engine configuration. Used commonly by  * {@link AtomPushHandler properties file} and {@link AtomPushBean spring} configuration schemes.  */
end_comment

begin_comment
comment|// TODO extract 'general rules' of interpretation in handler and bean and put here
end_comment

begin_class
specifier|final
class|class
name|AtomPushEngineConfigurator
block|{
specifier|private
name|Deliverer
name|deliverer
decl_stmt|;
specifier|private
name|Converter
name|converter
decl_stmt|;
specifier|private
name|String
name|delivererClass
decl_stmt|;
specifier|private
name|String
name|converterClass
decl_stmt|;
specifier|private
name|String
name|batchSize
decl_stmt|;
specifier|private
name|String
name|batchCleanupTime
decl_stmt|;
specifier|private
name|String
name|delivererUrl
decl_stmt|;
specifier|private
name|String
name|retryTimeout
decl_stmt|;
specifier|private
name|String
name|retryPause
decl_stmt|;
specifier|private
name|String
name|retryPauseTime
decl_stmt|;
specifier|private
name|String
name|output
decl_stmt|;
specifier|private
name|String
name|multiplicity
decl_stmt|;
specifier|private
name|String
name|format
decl_stmt|;
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|delivererUrl
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|void
name|setRetryTimeout
parameter_list|(
name|String
name|retryTimeout
parameter_list|)
block|{
name|this
operator|.
name|retryTimeout
operator|=
name|retryTimeout
expr_stmt|;
block|}
specifier|public
name|void
name|setRetryPause
parameter_list|(
name|String
name|retryPause
parameter_list|)
block|{
name|this
operator|.
name|retryPause
operator|=
name|retryPause
expr_stmt|;
block|}
specifier|public
name|void
name|setRetryPauseTime
parameter_list|(
name|String
name|retryPauseTime
parameter_list|)
block|{
name|this
operator|.
name|retryPauseTime
operator|=
name|retryPauseTime
expr_stmt|;
block|}
specifier|public
name|void
name|setBatchCleanupTime
parameter_list|(
name|String
name|cleanupTime
parameter_list|)
block|{
name|this
operator|.
name|batchCleanupTime
operator|=
name|cleanupTime
expr_stmt|;
block|}
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|String
name|batchSize
parameter_list|)
block|{
name|this
operator|.
name|batchSize
operator|=
name|batchSize
expr_stmt|;
block|}
specifier|public
name|void
name|setDeliverer
parameter_list|(
name|Deliverer
name|deliverer
parameter_list|)
block|{
name|this
operator|.
name|deliverer
operator|=
name|deliverer
expr_stmt|;
block|}
specifier|public
name|void
name|setConverter
parameter_list|(
name|Converter
name|converter
parameter_list|)
block|{
name|this
operator|.
name|converter
operator|=
name|converter
expr_stmt|;
block|}
specifier|public
name|void
name|setDelivererClass
parameter_list|(
name|String
name|delivererClass
parameter_list|)
block|{
name|this
operator|.
name|delivererClass
operator|=
name|delivererClass
expr_stmt|;
block|}
specifier|public
name|void
name|setConverterClass
parameter_list|(
name|String
name|converterClass
parameter_list|)
block|{
name|this
operator|.
name|converterClass
operator|=
name|converterClass
expr_stmt|;
block|}
specifier|public
name|void
name|setOutput
parameter_list|(
name|String
name|output
parameter_list|)
block|{
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
block|}
specifier|public
name|void
name|setMultiplicity
parameter_list|(
name|String
name|multiplicity
parameter_list|)
block|{
name|this
operator|.
name|multiplicity
operator|=
name|multiplicity
expr_stmt|;
block|}
specifier|public
name|void
name|setFormat
parameter_list|(
name|String
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
specifier|public
name|AtomPushEngine
name|createEngine
parameter_list|()
block|{
name|Deliverer
name|d
init|=
name|deliverer
decl_stmt|;
name|Converter
name|c
init|=
name|converter
decl_stmt|;
name|int
name|batch
init|=
name|parseInt
argument_list|(
name|batchSize
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|int
name|batchTime
init|=
name|parseInt
argument_list|(
name|batchCleanupTime
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|delivererUrl
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|delivererClass
operator|!=
literal|null
condition|)
block|{
name|d
operator|=
name|createDeliverer
argument_list|(
name|delivererClass
argument_list|,
name|delivererUrl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|d
operator|=
operator|new
name|WebClientDeliverer
argument_list|(
name|delivererUrl
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Either url, deliverer or "
operator|+
literal|"deliverer class with url must be setup"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|converterClass
operator|!=
literal|null
condition|)
block|{
name|c
operator|=
name|createConverter
argument_list|(
name|converterClass
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Output
name|out
init|=
name|parseEnum
argument_list|(
name|output
argument_list|,
name|Output
operator|.
name|FEED
argument_list|,
name|Output
operator|.
name|class
argument_list|)
decl_stmt|;
name|Multiplicity
name|defaultMul
init|=
name|out
operator|==
name|Output
operator|.
name|FEED
condition|?
name|Multiplicity
operator|.
name|MANY
else|:
name|batch
operator|>
literal|1
condition|?
name|Multiplicity
operator|.
name|MANY
else|:
name|Multiplicity
operator|.
name|ONE
decl_stmt|;
name|Multiplicity
name|mul
init|=
name|parseEnum
argument_list|(
name|multiplicity
argument_list|,
name|defaultMul
argument_list|,
name|Multiplicity
operator|.
name|class
argument_list|)
decl_stmt|;
name|Format
name|form
init|=
name|parseEnum
argument_list|(
name|format
argument_list|,
name|Format
operator|.
name|CONTENT
argument_list|,
name|Format
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|out
operator|==
name|Output
operator|.
name|FEED
condition|)
block|{
name|c
operator|=
operator|new
name|StandardConverter
argument_list|(
name|out
argument_list|,
name|mul
argument_list|,
name|form
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|c
operator|=
operator|new
name|StandardConverter
argument_list|(
name|out
argument_list|,
name|mul
argument_list|,
name|form
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|retryPause
operator|!=
literal|null
condition|)
block|{
name|int
name|timeout
init|=
name|parseInt
argument_list|(
name|retryTimeout
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|int
name|pause
init|=
name|parseInt
argument_list|(
name|retryPauseTime
argument_list|,
literal|1
argument_list|,
literal|30
argument_list|)
decl_stmt|;
name|boolean
name|linear
init|=
operator|!
name|retryPause
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"exponential"
argument_list|)
decl_stmt|;
name|d
operator|=
operator|new
name|RetryingDeliverer
argument_list|(
name|d
argument_list|,
name|timeout
argument_list|,
name|pause
argument_list|,
name|linear
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|AtomPushEngine
name|engine
init|=
operator|new
name|AtomPushEngine
argument_list|()
decl_stmt|;
name|engine
operator|.
name|setDeliverer
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setConverter
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setBatchSize
argument_list|(
name|batch
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setBatchTime
argument_list|(
name|batchTime
argument_list|)
expr_stmt|;
return|return
name|engine
return|;
block|}
specifier|private
name|Deliverer
name|createDeliverer
parameter_list|(
name|String
name|clazz
parameter_list|,
name|String
name|url
parameter_list|)
block|{
try|try
block|{
name|Constructor
argument_list|<
name|Deliverer
argument_list|>
name|ctor
init|=
name|loadClass
argument_list|(
name|clazz
argument_list|,
name|Deliverer
operator|.
name|class
argument_list|)
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|ctor
operator|.
name|newInstance
argument_list|(
name|url
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Converter
name|createConverter
parameter_list|(
name|String
name|clazz
parameter_list|)
block|{
try|try
block|{
name|Constructor
argument_list|<
name|Converter
argument_list|>
name|ctor
init|=
name|loadClass
argument_list|(
name|clazz
argument_list|,
name|Converter
operator|.
name|class
argument_list|)
operator|.
name|getConstructor
argument_list|()
decl_stmt|;
return|return
name|ctor
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|loadClass
parameter_list|(
name|String
name|clazz
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|ifaceClass
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|ClassLoader
name|cl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|cl
operator|.
name|loadClass
argument_list|(
name|clazz
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
try|try
block|{
comment|// clazz could be shorted (stripped package name) retry for interface location
name|String
name|pkg
init|=
name|ifaceClass
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|clazz2
init|=
name|pkg
operator|+
literal|"."
operator|+
name|clazz
decl_stmt|;
return|return
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|cl
operator|.
name|loadClass
argument_list|(
name|clazz2
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|" or "
operator|+
name|e1
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|int
name|parseInt
parameter_list|(
name|String
name|property
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
try|try
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|property
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
return|return
name|defaultValue
return|;
block|}
block|}
specifier|private
name|int
name|parseInt
parameter_list|(
name|String
name|property
parameter_list|,
name|int
name|lessThan
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|int
name|ret
init|=
name|parseInt
argument_list|(
name|property
argument_list|,
name|defaultValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|<
name|lessThan
condition|)
block|{
name|ret
operator|=
name|defaultValue
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
parameter_list|>
name|T
name|parseEnum
parameter_list|(
name|String
name|value
parameter_list|,
name|T
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
try|try
block|{
return|return
name|Enum
operator|.
name|valueOf
argument_list|(
name|enumClass
argument_list|,
name|value
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|defaultValue
return|;
block|}
block|}
block|}
end_class

end_unit

