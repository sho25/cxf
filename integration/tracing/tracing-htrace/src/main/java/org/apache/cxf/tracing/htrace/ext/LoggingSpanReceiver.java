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
name|tracing
operator|.
name|htrace
operator|.
name|ext
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|function
operator|.
name|Function
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
name|util
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|core
operator|.
name|HTraceConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|core
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|core
operator|.
name|SpanReceiver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|core
operator|.
name|TimelineAnnotation
import|;
end_import

begin_comment
comment|/**  * Span receiver implementation which outputs spans into logs.   */
end_comment

begin_class
specifier|public
class|class
name|LoggingSpanReceiver
extends|extends
name|SpanReceiver
block|{
specifier|public
specifier|static
specifier|final
name|String
name|LOG_LEVEL_KEY
init|=
literal|"cxf.log.level"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOG_LEVEL_ERROR
init|=
name|Level
operator|.
name|SEVERE
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOG_LEVEL_DEBUG
init|=
name|Level
operator|.
name|FINE
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOG_LEVEL_INFO
init|=
name|Level
operator|.
name|INFO
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOG_LEVEL_WARN
init|=
name|Level
operator|.
name|WARNING
operator|.
name|getName
argument_list|()
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
name|LoggingSpanReceiver
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Level
name|level
decl_stmt|;
specifier|public
name|LoggingSpanReceiver
parameter_list|(
name|HTraceConfiguration
name|conf
parameter_list|)
block|{
name|level
operator|=
name|Level
operator|.
name|parse
argument_list|(
name|conf
operator|.
name|get
argument_list|(
name|LOG_LEVEL_KEY
argument_list|,
name|LOG_LEVEL_DEBUG
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|receiveSpan
parameter_list|(
name|Span
name|span
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|level
argument_list|,
name|toString
argument_list|(
name|span
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{     }
comment|/**      * Sample log statements:      *       * INFO org.apache.cxf.tracing.htrace.ext.LoggingSpanReceiver - spanId=e5999a29a1ea468201acac30ec04ae39       *   tracerId="tracer-server/192.168.0.100" start=1488049449621 stop=1488049451623 description="Get Employees"       *   parents=[e5999a29a1ea4682d346ae17e51e0bd4] kvs=[] timelines=[[time=1488049451623       *   message="Getting all employees"]]      *       * INFO org.apache.cxf.tracing.htrace.ext.LoggingSpanReceiver - spanId=e5999a29a1ea4682ac0a9ad638e084ed       *   tracerId="tracer-client/192.168.0.100" start=1488049449074 stop=1488049454894       *   description="GET http://localhost:8282/rest/api/people" parents=[] kvs=[] timelines=[]      */
specifier|private
name|String
name|toString
parameter_list|(
name|TimelineAnnotation
name|annotation
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|append
argument_list|(
name|sb
argument_list|,
literal|"time"
argument_list|,
name|annotation
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|sb
argument_list|,
literal|"message"
argument_list|,
name|annotation
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|toString
parameter_list|(
name|Span
name|span
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|span
operator|.
name|getSpanId
argument_list|()
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
literal|"spanId"
argument_list|,
name|span
operator|.
name|getSpanId
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|tracerId
init|=
name|span
operator|.
name|getTracerId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|tracerId
argument_list|)
condition|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
literal|"tracerId"
argument_list|,
name|tracerId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|span
operator|.
name|getStartTimeMillis
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
literal|"start"
argument_list|,
name|span
operator|.
name|getStartTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|span
operator|.
name|getStopTimeMillis
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
literal|"stop"
argument_list|,
name|span
operator|.
name|getStopTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|span
operator|.
name|getDescription
argument_list|()
argument_list|)
condition|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
literal|"description"
argument_list|,
name|span
operator|.
name|getDescription
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|append
argument_list|(
name|sb
argument_list|,
literal|"parents"
argument_list|,
name|span
operator|.
name|getParents
argument_list|()
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|sb
argument_list|,
literal|"kvs"
argument_list|,
name|span
operator|.
name|getKVAnnotations
argument_list|()
argument_list|)
expr_stmt|;
name|append
argument_list|(
name|sb
argument_list|,
literal|"timelines"
argument_list|,
name|span
operator|.
name|getTimelineAnnotations
argument_list|()
argument_list|,
name|t
lambda|->
literal|"["
operator|+
name|toString
argument_list|(
name|t
argument_list|)
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|append
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|inner
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|values
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|append
argument_list|(
name|inner
argument_list|,
name|quote
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|append
argument_list|(
name|sb
argument_list|,
name|key
argument_list|,
name|inner
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|append
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|Collection
argument_list|<
name|T
argument_list|>
name|values
parameter_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
name|stringifyer
parameter_list|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
name|key
argument_list|,
name|Arrays
operator|.
name|toString
argument_list|(
name|values
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|stringifyer
operator|::
name|apply
argument_list|)
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|append
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|T
index|[]
name|values
parameter_list|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
name|key
argument_list|,
name|Arrays
operator|.
name|toString
argument_list|(
name|Arrays
operator|.
name|stream
argument_list|(
name|values
argument_list|)
operator|.
name|map
argument_list|(
name|T
operator|::
name|toString
argument_list|)
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|append
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|long
name|value
parameter_list|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
name|key
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|append
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|append
argument_list|(
name|sb
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|append
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|quoted
parameter_list|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
name|quote
argument_list|(
name|sb
argument_list|,
name|value
argument_list|,
name|quoted
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|quote
parameter_list|(
name|String
name|value
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|quote
argument_list|(
name|sb
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|quote
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|quoted
parameter_list|)
block|{
if|if
condition|(
name|quoted
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|quoted
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

