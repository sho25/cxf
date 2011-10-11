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
name|wsn
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
name|Iterator
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
name|javax
operator|.
name|jms
operator|.
name|Topic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|command
operator|.
name|ActiveMQTopic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|oasis_open
operator|.
name|docs
operator|.
name|wsn
operator|.
name|b_2
operator|.
name|TopicExpressionType
import|;
end_import

begin_class
specifier|public
class|class
name|JmsTopicExpressionConverter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SIMPLE_DIALECT
init|=
literal|"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple"
decl_stmt|;
specifier|public
name|TopicExpressionType
name|toTopicExpression
parameter_list|(
name|Topic
name|topic
parameter_list|)
block|{
return|return
name|toTopicExpression
argument_list|(
name|topic
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|TopicExpressionType
name|toTopicExpression
parameter_list|(
name|ActiveMQTopic
name|topic
parameter_list|)
block|{
return|return
name|toTopicExpression
argument_list|(
name|topic
operator|.
name|getPhysicalName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|TopicExpressionType
name|toTopicExpression
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|TopicExpressionType
name|answer
init|=
operator|new
name|TopicExpressionType
argument_list|()
decl_stmt|;
name|answer
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setDialect
argument_list|(
name|SIMPLE_DIALECT
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
specifier|public
name|ActiveMQTopic
name|toActiveMQTopic
parameter_list|(
name|List
argument_list|<
name|TopicExpressionType
argument_list|>
name|topics
parameter_list|)
throws|throws
name|InvalidTopicException
block|{
if|if
condition|(
name|topics
operator|==
literal|null
operator|||
name|topics
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|size
init|=
name|topics
operator|.
name|size
argument_list|()
decl_stmt|;
name|ActiveMQTopic
name|childrenDestinations
index|[]
init|=
operator|new
name|ActiveMQTopic
index|[
name|size
index|]
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|childrenDestinations
index|[
name|i
index|]
operator|=
name|toActiveMQTopic
argument_list|(
name|topics
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ActiveMQTopic
name|topic
init|=
operator|new
name|ActiveMQTopic
argument_list|()
decl_stmt|;
name|topic
operator|.
name|setCompositeDestinations
argument_list|(
name|childrenDestinations
argument_list|)
expr_stmt|;
return|return
name|topic
return|;
block|}
specifier|public
name|ActiveMQTopic
name|toActiveMQTopic
parameter_list|(
name|TopicExpressionType
name|topic
parameter_list|)
throws|throws
name|InvalidTopicException
block|{
name|String
name|dialect
init|=
name|topic
operator|.
name|getDialect
argument_list|()
decl_stmt|;
if|if
condition|(
name|dialect
operator|==
literal|null
operator|||
name|SIMPLE_DIALECT
operator|.
name|equals
argument_list|(
name|dialect
argument_list|)
condition|)
block|{
for|for
control|(
name|Iterator
name|iter
init|=
name|topic
operator|.
name|getContent
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ActiveMQTopic
name|answer
init|=
name|createActiveMQTopicFromContent
argument_list|(
name|iter
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
throw|throw
operator|new
name|InvalidTopicException
argument_list|(
literal|"No topic name available topic: "
operator|+
name|topic
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidTopicException
argument_list|(
literal|"Topic dialect: "
operator|+
name|dialect
operator|+
literal|" not supported"
argument_list|)
throw|;
block|}
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
specifier|protected
name|ActiveMQTopic
name|createActiveMQTopicFromContent
parameter_list|(
name|Object
name|contentItem
parameter_list|)
block|{
if|if
condition|(
name|contentItem
operator|instanceof
name|String
condition|)
block|{
return|return
operator|new
name|ActiveMQTopic
argument_list|(
operator|(
operator|(
name|String
operator|)
name|contentItem
operator|)
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|contentItem
operator|instanceof
name|QName
condition|)
block|{
return|return
name|createActiveMQTopicFromQName
argument_list|(
operator|(
name|QName
operator|)
name|contentItem
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|ActiveMQTopic
name|createActiveMQTopicFromQName
parameter_list|(
name|QName
name|qName
parameter_list|)
block|{
return|return
operator|new
name|ActiveMQTopic
argument_list|(
name|qName
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

