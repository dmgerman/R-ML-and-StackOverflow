#!/usr/bin/perl

use Date::Parse;
use strict;
use POSIX 'strftime';

$/ = "\nFrom ";

my $prevDate;
while (<>) {
    # split header from rest
    my $split = index($_, "\n\n");
    die "not a record [$_]" if $split == 0;

    my $h  = "FROM: " . substr($_, 0, $split+1);

    my %f = split_f($h);

    foreach my $k (sort keys (%f)) {
        #print "[$k] -> $f{$k}\n";
        #        print "$k\n";
        my $val = $f{$k};
    
    }
    my $fDate = $f{"Date"};
    #
#    my $dateO = new Date::Manip::Delta;
#    my $error = $dateO->parse($fDate);

#$date->parse('1988-12-13');
#print $date->printf('%O -> %s');
#    print STDERR $dateO->printf('%O -> %s'), "\n";

    #my $date = ParseDate($fDate);
    my $dates;
    my $date = str2time($fDate);
    my ($ss,$mm,$hh,$day,$month,$year,$zone) = strptime($fDate);
    $year += 1900;
    if ($year < 1980 or $year > 2020) {
        print STDERR "illegal date [$date][$year][$fDate] -> [$prevDate]\n";
        $dates = $prevDate;
    } else {
        $dates = sprintf("%4d-%02d-%02d %02d:%02d", $year, $month+1, $day, $hh, $mm);
    }

    $prevDate = $dates;

    if ($f{"Message-ID"}) {
        my $mid = $f{"Message-ID"};
        my $fmid = Format_mid($f{"Message-ID"});
        $f{"From"} = lc($f{"From"});
        $f{"From"} =~ s/;/SEMICOLON/g;
        my $email = Convert_Mail($f{"From"});
        die if not $f{"From"} =~ /\(/;
        print Format_mid($f{"Message-ID"}), ";", Format_From($f{"From"}), ";", $f{"In-Reply-To"}, ";", $email ,";$dates;$fDate\n";
    } else {
        print STDERR "Message had no Message-ID\n";
    }
}


sub split_f {
    my ($f) = @_;
    $f =~ s/;/SEMICOLON/g;
    my %r = ();
    $f =~ s/\n\W+/ /g;
    my @lines = split(/\n/, $f);
    foreach my $l (@lines) {
        my $pos = index($l, ": ");
        die "illegal line [$l]\n" if $pos == 0;
        
        my $k = substr($l, 0, $pos);
        my $f = substr($l, $pos+2);
#        die "already defined $k [$r{$k}][$f]" if defined($r{$k});
        $r{$k} = $f;
    }

#    print ">>>[", $r{"In-Reply-To"}, "]\n" if defined $r{"In-Reply-To"};
#    print ">>>[", $r{"References"}, "]\n" if defined $r{"References"};

    if (defined($r{"In-Reply-To"}) or defined($r{"References"})) {
        $r{"In-Reply-To"} = "YES";
    } else {
        $r{"In-Reply-To"} = "NO";
    }
    return %r;
}

sub Format_mid {
    my ($s) = @_;
    my $pos = index($s, "@");
    die "From does not have ( [$s]" if $pos == 0;
    $s = substr($s,0,$pos);
    $s =~ s/^<//;
    return $s;
}

sub Format_From {
    my ($s) = @_;
    my $pos = index($s, "(");
    die "From does not have ( [$s]" if $pos == 0;
    $s = substr($s,0,$pos-1);
    $s =~ s/\W+$//;
    $s =~ s/ at /@/;
    return $s;
}

sub Convert_Mail {
    my ($s) = @_;
    my $n = Format_From($s);
    $s =~ /\((.+)\)/;
    my $name = $1;
    
    return $name . " <$n>";
}
