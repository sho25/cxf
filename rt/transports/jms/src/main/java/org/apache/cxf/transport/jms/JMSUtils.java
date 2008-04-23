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
name|transport
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|Properties
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
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
specifier|final
class|class
name|JMSUtils
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
name|JMSUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JMSUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|Context
name|getInitialContext
parameter_list|(
name|AddressType
name|addrType
parameter_list|)
throws|throws
name|NamingException
block|{
name|Properties
name|env
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|populateContextEnvironment
argument_list|(
name|addrType
argument_list|,
name|env
argument_list|)
expr_stmt|;
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
name|Enumeration
name|props
init|=
name|env
operator|.
name|propertyNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|props
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|props
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|env
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Context property: "
operator|+
name|name
operator|+
literal|" | "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|Context
name|context
init|=
operator|new
name|InitialContext
argument_list|(
name|env
argument_list|)
decl_stmt|;
return|return
name|context
return|;
block|}
specifier|protected
specifier|static
name|void
name|populateContextEnvironment
parameter_list|(
name|AddressType
name|addrType
parameter_list|,
name|Properties
name|env
parameter_list|)
block|{
name|java
operator|.
name|util
operator|.
name|ListIterator
name|listIter
init|=
name|addrType
operator|.
name|getJMSNamingProperty
argument_list|()
operator|.
name|listIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|listIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|JMSNamingPropertyType
name|propertyPair
init|=
operator|(
name|JMSNamingPropertyType
operator|)
name|listIter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|propertyPair
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|env
operator|.
name|setProperty
argument_list|(
name|propertyPair
operator|.
name|getName
argument_list|()
argument_list|,
name|propertyPair
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|int
name|getJMSDeliveryMode
parameter_list|(
name|JMSMessageHeadersType
name|headers
parameter_list|)
block|{
name|int
name|deliveryMode
init|=
name|Message
operator|.
name|DEFAULT_DELIVERY_MODE
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetJMSDeliveryMode
argument_list|()
condition|)
block|{
name|deliveryMode
operator|=
name|headers
operator|.
name|getJMSDeliveryMode
argument_list|()
expr_stmt|;
block|}
return|return
name|deliveryMode
return|;
block|}
specifier|public
specifier|static
name|int
name|getJMSPriority
parameter_list|(
name|JMSMessageHeadersType
name|headers
parameter_list|)
block|{
name|int
name|priority
init|=
name|Message
operator|.
name|DEFAULT_PRIORITY
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetJMSPriority
argument_list|()
condition|)
block|{
name|priority
operator|=
name|headers
operator|.
name|getJMSPriority
argument_list|()
expr_stmt|;
block|}
return|return
name|priority
return|;
block|}
specifier|public
specifier|static
name|long
name|getTimeToLive
parameter_list|(
name|JMSMessageHeadersType
name|headers
parameter_list|)
block|{
name|long
name|ttl
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetTimeToLive
argument_list|()
condition|)
block|{
name|ttl
operator|=
name|headers
operator|.
name|getTimeToLive
argument_list|()
expr_stmt|;
block|}
return|return
name|ttl
return|;
block|}
specifier|public
specifier|static
name|String
name|getCorrelationId
parameter_list|(
name|JMSMessageHeadersType
name|headers
parameter_list|)
block|{
name|String
name|correlationId
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetJMSCorrelationID
argument_list|()
condition|)
block|{
name|correlationId
operator|=
name|headers
operator|.
name|getJMSCorrelationID
argument_list|()
expr_stmt|;
block|}
return|return
name|correlationId
return|;
block|}
specifier|public
specifier|static
name|void
name|setMessageProperties
parameter_list|(
name|JMSMessageHeadersType
name|headers
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetProperty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|JMSPropertyType
argument_list|>
name|props
init|=
name|headers
operator|.
name|getProperty
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|props
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|message
operator|.
name|setStringProperty
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|,
name|props
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

