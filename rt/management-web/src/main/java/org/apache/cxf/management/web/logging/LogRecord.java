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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
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
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|Validate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|EqualsBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|HashCodeBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|ToStringBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|ToStringStyle
import|;
end_import

begin_comment
comment|/**  * Log entry serializable to XML. Based on common set of {@link java.util.logging.LogRecord} and  * {@link org.apache.log4j.spi.LoggingEvent} attributes.  *<p>  * LogRecord are never null; if some attributes are not set (e.g. logger name, or rendered cause taken from  * Throwable) empty strings are returned.  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/log"
argument_list|)
specifier|public
class|class
name|LogRecord
block|{
annotation|@
name|XmlTransient
specifier|private
name|String
name|id
init|=
literal|"uuid:"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|private
name|Date
name|eventTimestamp
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
specifier|private
name|LogLevel
name|level
init|=
name|LogLevel
operator|.
name|INFO
decl_stmt|;
specifier|private
name|String
name|message
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|loggerName
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|threadName
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|throwable
init|=
literal|""
decl_stmt|;
specifier|public
name|LogRecord
parameter_list|()
block|{              }
specifier|public
name|LogRecord
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|LogRecord
parameter_list|(
name|LogRecord
name|copy
parameter_list|)
block|{
name|this
operator|.
name|eventTimestamp
operator|=
name|copy
operator|.
name|getEventTimestamp
argument_list|()
expr_stmt|;
name|this
operator|.
name|level
operator|=
name|copy
operator|.
name|getLevel
argument_list|()
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|copy
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|this
operator|.
name|loggerName
operator|=
name|copy
operator|.
name|getLoggerName
argument_list|()
expr_stmt|;
name|this
operator|.
name|threadName
operator|=
name|copy
operator|.
name|getThreadName
argument_list|()
expr_stmt|;
name|this
operator|.
name|throwable
operator|=
name|copy
operator|.
name|getThrowable
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates this object from JUL LogRecord. Most attributes are copied, others are converted as follows:      * raw {@link java.util.logging.LogRecord#getMessage() message} is formatted with      * {@link java.util.logging.LogRecord#getParameters() parameters} using {@link MessageFormat}, attached      * {@link java.util.logging.LogRecord#getThrown() throwable} has full stack trace dumped, and log levels      * are mapped as specified in {@link LogRecord}.      *       * @param julRecord log record to convert.      * @return conversion result.      */
specifier|public
specifier|static
name|LogRecord
name|fromJUL
parameter_list|(
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|LogRecord
name|julRecord
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|julRecord
argument_list|,
literal|"julRecord is null"
argument_list|)
expr_stmt|;
name|LogRecord
name|record
init|=
operator|new
name|LogRecord
argument_list|()
decl_stmt|;
name|record
operator|.
name|setEventTimestamp
argument_list|(
operator|new
name|Date
argument_list|(
name|julRecord
operator|.
name|getMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|record
operator|.
name|setLevel
argument_list|(
name|LogLevel
operator|.
name|fromJUL
argument_list|(
name|julRecord
operator|.
name|getLevel
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|record
operator|.
name|setLoggerName
argument_list|(
name|julRecord
operator|.
name|getLoggerName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|julRecord
operator|.
name|getThrown
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|record
operator|.
name|setThrowable
argument_list|(
name|julRecord
operator|.
name|getThrown
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|julRecord
operator|.
name|getParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|record
operator|.
name|setMessage
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
name|julRecord
operator|.
name|getMessage
argument_list|()
argument_list|,
name|julRecord
operator|.
name|getParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|record
operator|.
name|setMessage
argument_list|(
name|julRecord
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|record
operator|.
name|setThreadName
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|julRecord
operator|.
name|getThreadID
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|record
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/log"
argument_list|)
specifier|public
name|Date
name|getEventTimestamp
parameter_list|()
block|{
return|return
name|eventTimestamp
return|;
block|}
specifier|public
name|void
name|setEventTimestamp
parameter_list|(
name|Date
name|eventTimestamp
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|eventTimestamp
argument_list|,
literal|"eventTimestamp is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventTimestamp
operator|=
name|eventTimestamp
expr_stmt|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/log"
argument_list|)
specifier|public
name|LogLevel
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
specifier|public
name|void
name|setLevel
parameter_list|(
name|LogLevel
name|level
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|level
argument_list|,
literal|"level is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
comment|/**      * Formatted message with parameters filled in.      */
annotation|@
name|XmlElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/log"
argument_list|)
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|renderedMessage
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|level
argument_list|,
literal|"message is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|renderedMessage
expr_stmt|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/log"
argument_list|)
specifier|public
name|String
name|getLoggerName
parameter_list|()
block|{
return|return
name|loggerName
return|;
block|}
specifier|public
name|void
name|setLoggerName
parameter_list|(
name|String
name|loggerName
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|level
argument_list|,
literal|"loggerName is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|loggerName
operator|=
name|loggerName
expr_stmt|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/log"
argument_list|)
specifier|public
name|String
name|getThreadName
parameter_list|()
block|{
return|return
name|threadName
return|;
block|}
specifier|public
name|void
name|setThreadName
parameter_list|(
name|String
name|threadName
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|level
argument_list|,
literal|"threadName is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|threadName
operator|=
name|threadName
expr_stmt|;
block|}
comment|/**      * Full stack trace of {@link Throwable} associated with log record.      */
annotation|@
name|XmlElement
argument_list|(
name|namespace
operator|=
literal|"http://cxf.apache.org/log"
argument_list|)
specifier|public
name|String
name|getThrowable
parameter_list|()
block|{
return|return
name|throwable
return|;
block|}
specifier|public
name|void
name|setThrowable
parameter_list|(
name|String
name|throwable
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|throwable
argument_list|,
literal|"throwable is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|throwable
operator|=
name|throwable
expr_stmt|;
block|}
specifier|public
name|void
name|setThrowable
parameter_list|(
name|Throwable
name|thr
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|thr
argument_list|,
literal|"throwable is null"
argument_list|)
expr_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|thr
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|throwable
operator|=
name|sw
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|EqualsBuilder
operator|.
name|reflectionEquals
argument_list|(
name|obj
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|HashCodeBuilder
operator|.
name|reflectionHashCode
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ToStringBuilder
operator|.
name|reflectionToString
argument_list|(
name|this
argument_list|,
name|ToStringStyle
operator|.
name|SHORT_PREFIX_STYLE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

