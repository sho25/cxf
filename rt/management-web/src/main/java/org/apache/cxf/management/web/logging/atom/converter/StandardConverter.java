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
operator|.
name|converter
package|;
end_package

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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|JAXBContext
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
name|JAXBException
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
name|abdera
operator|.
name|Abdera
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|factory
operator|.
name|Factory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Content
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|ExtensibleElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
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
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|atom
operator|.
name|AbstractEntryBuilder
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
name|jaxrs
operator|.
name|ext
operator|.
name|atom
operator|.
name|AbstractFeedBuilder
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
name|LogRecord
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
name|LogRecords
import|;
end_import

begin_comment
comment|/**  * Converter producing ATOM Feeds on standalone Entries with LogRecords or LogRecordsLists embedded as content  * or extension. For configuration details see constructor documentation.  */
end_comment

begin_class
specifier|public
class|class
name|StandardConverter
implements|implements
name|Converter
block|{
comment|/** Conversion output */
specifier|public
enum|enum
name|Output
block|{
name|FEED
block|,
name|ENTRY
block|}
comment|/** Quantities of entries in feed or logrecords in entry */
specifier|public
enum|enum
name|Multiplicity
block|{
name|ONE
block|,
name|MANY
block|}
comment|/** Entity format */
specifier|public
enum|enum
name|Format
block|{
name|CONTENT
block|,
name|EXTENSION
block|}
specifier|private
name|Factory
name|factory
decl_stmt|;
specifier|private
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|DateFormat
name|df
decl_stmt|;
specifier|private
name|Converter
name|worker
decl_stmt|;
specifier|private
name|AbstractFeedBuilder
argument_list|<
name|List
argument_list|<
name|LogRecord
argument_list|>
argument_list|>
name|feedBuilder
decl_stmt|;
specifier|private
name|AbstractEntryBuilder
argument_list|<
name|List
argument_list|<
name|LogRecord
argument_list|>
argument_list|>
name|entryBuilder
decl_stmt|;
comment|/**      * Creates configured converter with default post-processing of feeds/entries mandatory properties.      * Regardless of "format", combination of "output" and "multiplicity" flags can be interpreted as follow:      *<ul>      *<li>ENTRY ONE - for each log record one entry is produced, converter returns list of entries</li>      *<li>ENTRY MANY - list of log records is packed in one entry, converter return one entry.</li>      *<li>FEED ONE - list of log records is packed in one entry, entry is inserted to feed, converter returns      * one feed.</li>      *<li>FEED MANY - for each log record one entry is produced, entries are collected in one feed, converter      * returns one feed.</li>      *</ul>      *       * @param output whether root elements if Feed or Entry (e.g. for AtomPub).      * @param multiplicity for output==FEED it is multiplicity of entities in feed for output==ENTITY it is      *            multiplicity of log records in entity.      * @param format log records embedded as entry content or extension.      */
specifier|public
name|StandardConverter
parameter_list|(
name|Output
name|output
parameter_list|,
name|Multiplicity
name|multiplicity
parameter_list|,
name|Format
name|format
parameter_list|)
block|{
name|this
argument_list|(
name|output
argument_list|,
name|multiplicity
argument_list|,
name|format
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates configured converter with feeds/entries post-processing based on data provided by feed and      * entry builders.      */
specifier|public
name|StandardConverter
parameter_list|(
name|Output
name|output
parameter_list|,
name|Multiplicity
name|multiplicity
parameter_list|,
name|Format
name|format
parameter_list|,
name|AbstractFeedBuilder
argument_list|<
name|List
argument_list|<
name|LogRecord
argument_list|>
argument_list|>
name|feedBuilder
parameter_list|,
name|AbstractEntryBuilder
argument_list|<
name|List
argument_list|<
name|LogRecord
argument_list|>
argument_list|>
name|entryBuilder
parameter_list|)
block|{
name|Validate
operator|.
name|notNull
argument_list|(
name|output
argument_list|,
literal|"output is null"
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|multiplicity
argument_list|,
literal|"multiplicity is null"
argument_list|)
expr_stmt|;
name|Validate
operator|.
name|notNull
argument_list|(
name|format
argument_list|,
literal|"format is null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|feedBuilder
operator|=
name|feedBuilder
expr_stmt|;
name|this
operator|.
name|entryBuilder
operator|=
name|entryBuilder
expr_stmt|;
name|configure
argument_list|(
name|output
argument_list|,
name|multiplicity
argument_list|,
name|format
argument_list|)
expr_stmt|;
comment|//NOPMD
name|df
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
argument_list|)
expr_stmt|;
name|factory
operator|=
name|Abdera
operator|.
name|getNewFactory
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|LogRecords
operator|.
name|class
argument_list|,
name|LogRecord
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Element
argument_list|>
name|convert
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
return|return
name|worker
operator|.
name|convert
argument_list|(
name|records
argument_list|)
return|;
block|}
specifier|private
name|void
name|configure
parameter_list|(
specifier|final
name|Output
name|output
parameter_list|,
specifier|final
name|Multiplicity
name|multiplicity
parameter_list|,
specifier|final
name|Format
name|format
parameter_list|)
block|{
if|if
condition|(
name|output
operator|==
name|Output
operator|.
name|ENTRY
operator|&&
name|multiplicity
operator|==
name|Multiplicity
operator|.
name|ONE
condition|)
block|{
name|worker
operator|=
operator|new
name|Converter
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|Entry
argument_list|>
name|convert
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
return|return
name|createEntries
argument_list|(
name|format
argument_list|,
name|records
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
if|if
condition|(
name|output
operator|==
name|Output
operator|.
name|ENTRY
operator|&&
name|multiplicity
operator|==
name|Multiplicity
operator|.
name|MANY
condition|)
block|{
name|worker
operator|=
operator|new
name|Converter
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|Entry
argument_list|>
name|convert
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
comment|// produces one entry with list of all log records
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|createEntryFromList
argument_list|(
name|format
argument_list|,
name|records
argument_list|)
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
if|if
condition|(
name|output
operator|==
name|Output
operator|.
name|FEED
operator|&&
name|multiplicity
operator|==
name|Multiplicity
operator|.
name|ONE
condition|)
block|{
name|worker
operator|=
operator|new
name|Converter
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|Feed
argument_list|>
name|convert
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
comment|// produces one feed with one entry with list of all log records
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|createFeedWithSingleEntry
argument_list|(
name|format
argument_list|,
name|records
argument_list|)
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
if|if
condition|(
name|output
operator|==
name|Output
operator|.
name|FEED
operator|&&
name|multiplicity
operator|==
name|Multiplicity
operator|.
name|MANY
condition|)
block|{
name|worker
operator|=
operator|new
name|Converter
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|Feed
argument_list|>
name|convert
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
comment|// produces one feed with many entries, each entry with one log record
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|createFeed
argument_list|(
name|format
argument_list|,
name|records
argument_list|)
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
if|if
condition|(
name|worker
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported configuration"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|Entry
argument_list|>
name|createEntries
parameter_list|(
name|Format
name|format
parameter_list|,
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|>
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
name|records
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|createEntryFromRecord
argument_list|(
name|format
argument_list|,
name|records
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|entries
return|;
block|}
specifier|private
name|Entry
name|createEntryFromList
parameter_list|(
name|Format
name|format
parameter_list|,
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
name|Entry
name|e
init|=
name|createEntry
argument_list|(
name|records
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|format
operator|==
name|Format
operator|.
name|CONTENT
condition|)
block|{
name|setEntryContent
argument_list|(
name|e
argument_list|,
name|createContent
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setEntryContent
argument_list|(
name|e
argument_list|,
name|createExtension
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|e
return|;
block|}
specifier|private
name|Entry
name|createEntryFromRecord
parameter_list|(
name|Format
name|format
parameter_list|,
name|LogRecord
name|record
parameter_list|,
name|int
name|entryIndex
parameter_list|)
block|{
name|Entry
name|e
init|=
name|createEntry
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|record
argument_list|)
argument_list|,
name|entryIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|format
operator|==
name|Format
operator|.
name|CONTENT
condition|)
block|{
name|setEntryContent
argument_list|(
name|e
argument_list|,
name|createContent
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setEntryContent
argument_list|(
name|e
argument_list|,
name|createExtension
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|e
return|;
block|}
specifier|private
name|String
name|createContent
parameter_list|(
name|LogRecord
name|record
parameter_list|)
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|context
operator|.
name|createMarshaller
argument_list|()
operator|.
name|marshal
argument_list|(
name|record
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|writer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|createContent
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|LogRecords
name|list
init|=
operator|new
name|LogRecords
argument_list|()
decl_stmt|;
name|list
operator|.
name|setLogRecords
argument_list|(
name|records
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|createMarshaller
argument_list|()
operator|.
name|marshal
argument_list|(
name|list
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|writer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|ExtensibleElement
name|createExtension
parameter_list|(
name|LogRecord
name|record
parameter_list|)
block|{
name|ExtensibleElement
name|erec
init|=
name|factory
operator|.
name|newExtensionElement
argument_list|(
name|qn
argument_list|(
literal|"logRecord"
argument_list|)
argument_list|)
decl_stmt|;
comment|// forget about single line "addExtension().setText()" since
comment|// javac failure "org.apache.abdera.model.Element cannot be dereferenced"
name|Element
name|e
init|=
name|erec
operator|.
name|addExtension
argument_list|(
name|qn
argument_list|(
literal|"eventTimestamp"
argument_list|)
argument_list|)
decl_stmt|;
name|e
operator|.
name|setText
argument_list|(
name|toAtomDateFormat
argument_list|(
name|record
operator|.
name|getEventTimestamp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|=
name|erec
operator|.
name|addExtension
argument_list|(
name|qn
argument_list|(
literal|"level"
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|setText
argument_list|(
name|record
operator|.
name|getLevel
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|erec
operator|.
name|addExtension
argument_list|(
name|qn
argument_list|(
literal|"loggerName"
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|setText
argument_list|(
name|record
operator|.
name|getLoggerName
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|erec
operator|.
name|addExtension
argument_list|(
name|qn
argument_list|(
literal|"message"
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|setText
argument_list|(
name|record
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|erec
operator|.
name|addExtension
argument_list|(
name|qn
argument_list|(
literal|"threadName"
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|setText
argument_list|(
name|record
operator|.
name|getThreadName
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|=
name|erec
operator|.
name|addExtension
argument_list|(
name|qn
argument_list|(
literal|"throwable"
argument_list|)
argument_list|)
expr_stmt|;
name|e
operator|.
name|setText
argument_list|(
name|record
operator|.
name|getThrowable
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|erec
return|;
block|}
specifier|private
name|String
name|toAtomDateFormat
parameter_list|(
name|Date
name|d
parameter_list|)
block|{
name|String
name|date
init|=
name|df
operator|.
name|format
argument_list|(
name|d
argument_list|)
decl_stmt|;
comment|// timezone in date does not have semicolon as XML Date requires
comment|// e.g we have "2009-11-23T22:03:53.996+0100"
comment|// instead of "2009-11-23T22:03:53.996+01:00"
return|return
name|date
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|date
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
operator|+
literal|":"
operator|+
name|date
operator|.
name|substring
argument_list|(
name|date
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
return|;
block|}
specifier|private
name|QName
name|qn
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/log"
argument_list|,
name|name
argument_list|,
literal|"log"
argument_list|)
return|;
block|}
specifier|private
name|ExtensibleElement
name|createExtension
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
name|ExtensibleElement
name|list
init|=
name|factory
operator|.
name|newExtensionElement
argument_list|(
name|qn
argument_list|(
literal|"logRecords"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|LogRecord
name|rec
range|:
name|records
control|)
block|{
name|list
operator|.
name|addExtension
argument_list|(
name|createExtension
argument_list|(
name|rec
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
specifier|private
name|Entry
name|createEntry
parameter_list|(
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|,
name|int
name|entryIndex
parameter_list|)
block|{
name|Entry
name|entry
init|=
name|factory
operator|.
name|newEntry
argument_list|()
decl_stmt|;
name|setDefaultEntryProperties
argument_list|(
name|entry
argument_list|,
name|records
argument_list|,
name|entryIndex
argument_list|)
expr_stmt|;
return|return
name|entry
return|;
block|}
specifier|private
name|void
name|setEntryContent
parameter_list|(
name|Entry
name|e
parameter_list|,
name|String
name|content
parameter_list|)
block|{
name|e
operator|.
name|setContent
argument_list|(
name|content
argument_list|,
name|Content
operator|.
name|Type
operator|.
name|XML
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setEntryContent
parameter_list|(
name|Entry
name|e
parameter_list|,
name|ExtensibleElement
name|ext
parameter_list|)
block|{
name|e
operator|.
name|addExtension
argument_list|(
name|ext
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Feed
name|createFeedWithSingleEntry
parameter_list|(
name|Format
name|format
parameter_list|,
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
name|Feed
name|feed
init|=
name|factory
operator|.
name|newFeed
argument_list|()
decl_stmt|;
name|feed
operator|.
name|addEntry
argument_list|(
name|createEntryFromList
argument_list|(
name|format
argument_list|,
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|setDefaultFeedProperties
argument_list|(
name|feed
argument_list|,
name|records
argument_list|)
expr_stmt|;
return|return
name|feed
return|;
block|}
specifier|private
name|Feed
name|createFeed
parameter_list|(
name|Format
name|format
parameter_list|,
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
name|Feed
name|feed
init|=
name|factory
operator|.
name|newFeed
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Entry
argument_list|>
name|entries
init|=
name|createEntries
argument_list|(
name|format
argument_list|,
name|records
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
name|entry
range|:
name|entries
control|)
block|{
name|feed
operator|.
name|addEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|setDefaultFeedProperties
argument_list|(
name|feed
argument_list|,
name|records
argument_list|)
expr_stmt|;
return|return
name|feed
return|;
block|}
specifier|protected
name|void
name|setDefaultFeedProperties
parameter_list|(
name|Feed
name|feed
parameter_list|,
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|)
block|{
if|if
condition|(
name|feedBuilder
operator|!=
literal|null
condition|)
block|{
name|feed
operator|.
name|setId
argument_list|(
name|feedBuilder
operator|.
name|getId
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|feed
operator|.
name|addAuthor
argument_list|(
name|feedBuilder
operator|.
name|getAuthor
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|feed
operator|.
name|setTitle
argument_list|(
name|feedBuilder
operator|.
name|getTitle
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|feed
operator|.
name|setUpdated
argument_list|(
name|feedBuilder
operator|.
name|getUpdated
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|feed
operator|.
name|setBaseUri
argument_list|(
name|feedBuilder
operator|.
name|getBaseUri
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|categories
init|=
name|feedBuilder
operator|.
name|getCategories
argument_list|(
name|records
argument_list|)
decl_stmt|;
if|if
condition|(
name|categories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|category
range|:
name|categories
control|)
block|{
name|feed
operator|.
name|addCategory
argument_list|(
name|category
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|links
init|=
name|feedBuilder
operator|.
name|getLinks
argument_list|(
name|records
argument_list|)
decl_stmt|;
if|if
condition|(
name|links
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapEntry
range|:
name|links
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|feed
operator|.
name|addLink
argument_list|(
name|mapEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|mapEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|feed
operator|.
name|setId
argument_list|(
literal|"uuid:"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|feed
operator|.
name|addAuthor
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|feed
operator|.
name|setTitle
argument_list|(
literal|"CXF Service Log Entries"
argument_list|)
expr_stmt|;
name|feed
operator|.
name|setUpdated
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setDefaultEntryProperties
parameter_list|(
name|Entry
name|entry
parameter_list|,
name|List
argument_list|<
name|LogRecord
argument_list|>
name|records
parameter_list|,
name|int
name|entryIndex
parameter_list|)
block|{
if|if
condition|(
name|entryBuilder
operator|!=
literal|null
condition|)
block|{
name|entry
operator|.
name|setId
argument_list|(
name|entryBuilder
operator|.
name|getId
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|addAuthor
argument_list|(
name|entryBuilder
operator|.
name|getAuthor
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setTitle
argument_list|(
name|entryBuilder
operator|.
name|getTitle
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setUpdated
argument_list|(
name|entryBuilder
operator|.
name|getUpdated
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setBaseUri
argument_list|(
name|entryBuilder
operator|.
name|getBaseUri
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setSummary
argument_list|(
name|entryBuilder
operator|.
name|getSummary
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|categories
init|=
name|entryBuilder
operator|.
name|getCategories
argument_list|(
name|records
argument_list|)
decl_stmt|;
if|if
condition|(
name|categories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|category
range|:
name|categories
control|)
block|{
name|entry
operator|.
name|addCategory
argument_list|(
name|category
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|links
init|=
name|entryBuilder
operator|.
name|getLinks
argument_list|(
name|records
argument_list|)
decl_stmt|;
if|if
condition|(
name|links
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapEntry
range|:
name|links
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|entry
operator|.
name|addLink
argument_list|(
name|mapEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|mapEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|entry
operator|.
name|setPublished
argument_list|(
name|entryBuilder
operator|.
name|getPublished
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setSummary
argument_list|(
name|entryBuilder
operator|.
name|getSummary
argument_list|(
name|records
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|entry
operator|.
name|addAuthor
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
if|if
condition|(
name|records
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|entry
operator|.
name|setId
argument_list|(
literal|"uuid:"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setTitle
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Entry with %d log record(s)"
argument_list|,
name|records
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|entry
operator|.
name|setId
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setTitle
argument_list|(
literal|"Log record with level "
operator|+
name|records
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLevel
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setSummary
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLoggerName
argument_list|()
operator|+
literal|" : "
operator|+
name|records
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|records
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|entry
operator|.
name|setUpdated
argument_list|(
name|toAtomDateFormat
argument_list|(
name|records
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEventTimestamp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

