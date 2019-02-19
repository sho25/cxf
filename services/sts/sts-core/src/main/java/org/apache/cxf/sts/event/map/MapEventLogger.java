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
name|sts
operator|.
name|event
operator|.
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|Map
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

begin_class
specifier|public
class|class
name|MapEventLogger
implements|implements
name|MapEventListener
block|{
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
name|MapEventLogger
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|fieldOrder
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|logStacktrace
decl_stmt|;
specifier|private
name|boolean
name|logFieldname
decl_stmt|;
specifier|private
name|Level
name|logLevel
init|=
name|Level
operator|.
name|FINE
decl_stmt|;
specifier|private
name|String
name|format
decl_stmt|;
specifier|public
name|MapEventLogger
parameter_list|()
block|{
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|TIME
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|STATUS
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|DURATION
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|REMOTE_HOST
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|REMOTE_PORT
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|OPERATION
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|URL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|REALM
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|WS_SEC_PRINCIPAL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|ONBEHALFOF_PRINCIPAL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|ACTAS_PRINCIPAL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|VALIDATE_PRINCIPAL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|CANCEL_PRINCIPAL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|RENEW_PRINCIPAL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|TOKENTYPE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|KEYTYPE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|APPLIESTO
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|CLAIMS_PRIMARY
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|CLAIMS_SECONDARY
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|EXCEPTION
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fieldOrder
operator|.
name|add
argument_list|(
name|KEYS
operator|.
name|STACKTRACE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onEvent
parameter_list|(
name|MapEvent
name|event
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|map
init|=
name|event
operator|.
name|getProperties
argument_list|()
decl_stmt|;
specifier|final
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|fieldOrder
control|)
block|{
if|if
condition|(
name|this
operator|.
name|logFieldname
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|format
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
block|}
block|}
name|Exception
name|ex
init|=
operator|(
name|Exception
operator|)
name|map
operator|.
name|get
argument_list|(
name|KEYS
operator|.
name|EXCEPTION
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|logStacktrace
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|this
operator|.
name|logLevel
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|this
operator|.
name|logLevel
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|format
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|value
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Date
condition|)
block|{
name|DateFormat
name|dateFormat
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|format
operator|!=
literal|null
condition|)
block|{
name|dateFormat
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dateFormat
operator|=
name|DateFormat
operator|.
name|getDateTimeInstance
argument_list|(
name|DateFormat
operator|.
name|SHORT
argument_list|,
name|DateFormat
operator|.
name|MEDIUM
argument_list|)
expr_stmt|;
block|}
return|return
name|dateFormat
operator|.
name|format
argument_list|(
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
name|value
operator|==
literal|null
operator|)
condition|?
literal|"<null>"
else|:
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFieldOrder
parameter_list|()
block|{
return|return
name|fieldOrder
return|;
block|}
specifier|public
name|void
name|setFieldOrder
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|fieldOrder
parameter_list|)
block|{
name|this
operator|.
name|fieldOrder
operator|=
name|fieldOrder
expr_stmt|;
block|}
specifier|public
name|boolean
name|isLogStacktrace
parameter_list|()
block|{
return|return
name|logStacktrace
return|;
block|}
specifier|public
name|void
name|setLogStacktrace
parameter_list|(
name|boolean
name|logStacktrace
parameter_list|)
block|{
name|this
operator|.
name|logStacktrace
operator|=
name|logStacktrace
expr_stmt|;
block|}
specifier|public
name|boolean
name|isLogFieldname
parameter_list|()
block|{
return|return
name|logFieldname
return|;
block|}
specifier|public
name|void
name|setLogFieldname
parameter_list|(
name|boolean
name|logFieldname
parameter_list|)
block|{
name|this
operator|.
name|logFieldname
operator|=
name|logFieldname
expr_stmt|;
block|}
specifier|public
name|void
name|setDateFormat
parameter_list|(
name|String
name|dateFormat
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|dateFormat
expr_stmt|;
block|}
specifier|public
name|String
name|getLogLevel
parameter_list|()
block|{
return|return
name|logLevel
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLogLevel
parameter_list|(
name|String
name|logLevel
parameter_list|)
block|{
name|this
operator|.
name|logLevel
operator|=
name|Level
operator|.
name|parse
argument_list|(
name|logLevel
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

