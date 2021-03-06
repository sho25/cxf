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
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|BytesMessage
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
name|jms
operator|.
name|ObjectMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|StreamMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
import|;
end_import

begin_comment
comment|/**  * Converts jms messages to Objects and vice a versa.  *<p>  * String<=> {@link javax.jms.TextMessage}  * byte[]<=> {@link javax.jms.BytesMessage}  * Serializable object<=> {@link javax.jms.ObjectMessage}  */
end_comment

begin_class
specifier|public
class|class
name|JMSMessageConverter
block|{
specifier|public
name|Message
name|toMessage
parameter_list|(
name|Object
name|object
parameter_list|,
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|object
operator|instanceof
name|Message
condition|)
block|{
return|return
operator|(
name|Message
operator|)
name|object
return|;
block|}
elseif|else
if|if
condition|(
name|object
operator|instanceof
name|String
condition|)
block|{
return|return
name|session
operator|.
name|createTextMessage
argument_list|(
operator|(
name|String
operator|)
name|object
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|object
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|BytesMessage
name|message
init|=
name|session
operator|.
name|createBytesMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|writeBytes
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|object
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
elseif|else
if|if
condition|(
name|object
operator|instanceof
name|Serializable
condition|)
block|{
return|return
name|session
operator|.
name|createObjectMessage
argument_list|(
operator|(
name|Serializable
operator|)
name|object
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported type "
operator|+
name|nullSafeClassName
argument_list|(
name|object
argument_list|)
operator|+
literal|"."
operator|+
literal|" Valid types are: String, byte[], Serializable object."
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|nullSafeClassName
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|==
literal|null
condition|?
literal|"null"
else|:
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|Object
name|fromMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|message
operator|instanceof
name|TextMessage
condition|)
block|{
return|return
operator|(
operator|(
name|TextMessage
operator|)
name|message
operator|)
operator|.
name|getText
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|BytesMessage
condition|)
block|{
name|BytesMessage
name|message1
init|=
operator|(
name|BytesMessage
operator|)
name|message
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|message1
operator|.
name|getBodyLength
argument_list|()
index|]
decl_stmt|;
name|message1
operator|.
name|readBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|bytes
return|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|ObjectMessage
condition|)
block|{
return|return
operator|(
operator|(
name|ObjectMessage
operator|)
name|message
operator|)
operator|.
name|getObject
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|StreamMessage
condition|)
block|{
name|StreamMessage
name|streamMessage
init|=
operator|(
name|StreamMessage
operator|)
name|message
decl_stmt|;
return|return
name|streamMessage
operator|.
name|readObject
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|byte
index|[]
block|{}
return|;
block|}
block|}
block|}
end_class

end_unit

